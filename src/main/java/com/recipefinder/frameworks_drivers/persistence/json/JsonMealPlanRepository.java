package com.recipefinder.frameworks_drivers.persistence.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import com.recipefinder.entities.DinnerSlot;
import com.recipefinder.entities.MealPlan;
import com.recipefinder.entities.RecipeKey;
import com.recipefinder.entities.UserId;
import com.recipefinder.entities.WeekKey;
import com.recipefinder.frameworks_drivers.persistence.json.dto.DinnerSlotJson;
import com.recipefinder.frameworks_drivers.persistence.json.dto.MealPlanJson;
import com.recipefinder.interfaces.MealPlanConflictException;
import com.recipefinder.interfaces.MealPlanRepository;
import com.recipefinder.interfaces.MealPlanSnapshot;
import com.recipefinder.interfaces.MealPlanStorageException;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/** JSON storage adapter for weekly meal plans. */
public final class JsonMealPlanRepository
        implements MealPlanRepository {

    private static final Map<Path, Object> MONITORS =
            new ConcurrentHashMap<>();

    private static final System.Logger LOGGER =
            System.getLogger(JsonMealPlanRepository.class.getName());

    private final Path file;
    private final Object monitor;

    private final Gson gson = new GsonBuilder()
            .setStrictness(Strictness.STRICT)
            .setPrettyPrinting()
            .create();

    /** Structure of the complete scheduling JSON document. */
    private record StoreJson(
            int schemaVersion,
            List<MealPlanJson> plans) {
    }

    public JsonMealPlanRepository(Path file) {
        this.file = Objects.requireNonNull(file, "file")
                .toAbsolutePath()
                .normalize();

        if (this.file.getFileName() == null) {
            throw new IllegalArgumentException(
                    "Provide a file path for meal-plan storage.");
        }

        this.monitor = MONITORS.computeIfAbsent(
                this.file, ignored -> new Object());
    }

    @Override
    public MealPlanSnapshot load(WeekKey key)
            throws MealPlanStorageException {

        Objects.requireNonNull(key, "key");

        synchronized (monitor) {
            Map<WeekKey, MealPlanSnapshot> plans = readAll();

            return plans.getOrDefault(
                    key,
                    new MealPlanSnapshot(new MealPlan(key), 0));
        }
    }

    @Override
    public MealPlanSnapshot save(
            MealPlan plan,
            long expectedRevision)
            throws MealPlanConflictException, MealPlanStorageException {

        Objects.requireNonNull(plan, "plan");

        if (expectedRevision < 0) {
            throw new IllegalArgumentException(
                    "Expected revision cannot be negative.");
        }

        synchronized (monitor) {
            FileChannel channel = null;
            FileLock fileLock = null;

            try {
                Files.createDirectories(file.getParent());

                Path lockFile = file.resolveSibling(
                        file.getFileName() + ".lock");

                channel = FileChannel.open(
                        lockFile,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE);

                fileLock = channel.lock();

                // Read while holding the lock so checking and saving
                // happen together.
                Map<WeekKey, MealPlanSnapshot> plans = readAll();

                WeekKey key = plan.getWeekKey();

                MealPlanSnapshot current = plans.getOrDefault(
                        key,
                        new MealPlanSnapshot(new MealPlan(key), 0));

                if (current.revision() != expectedRevision) {
                    throw new MealPlanConflictException(
                            "The weekly plan changed. Reload it.");
                }

                long nextRevision =
                        Math.addExact(current.revision(), 1L);

                MealPlanSnapshot saved =
                        new MealPlanSnapshot(plan, nextRevision);

                // Replace only this user's selected week.
                plans.put(key, saved);

                List<MealPlanJson> encodedPlans =
                        plans.values().stream()
                                .map(this::encode)
                                .toList();

                String json = gson.toJson(
                        new StoreJson(1, encodedPlans));

                writeAtomically(file, json);

                return saved;

            } catch (IOException
                     | ArithmeticException
                     | JsonParseException
                     | OverlappingFileLockException exception) {

                throw new MealPlanStorageException(
                        "The weekly plan could not be saved.",
                        exception);

            } finally {
                // Cleanup errors must not turn a completed save
                // into a reported failed save.
                closeQuietly(fileLock);
                closeQuietly(channel);
            }
        }
    }

    private Map<WeekKey, MealPlanSnapshot> readAll()
            throws MealPlanStorageException {

        String json;

        try {
            json = Files.readString(
                    file, StandardCharsets.UTF_8);

        } catch (NoSuchFileException exception) {
            return new LinkedHashMap<>();

        } catch (IOException exception) {
            throw new MealPlanStorageException(
                    "The meal-plan file could not be read.",
                    exception);
        }

        try {
            StoreJson store =
                    gson.fromJson(json, StoreJson.class);

            if (store == null
                    || store.schemaVersion() != 1
                    || store.plans() == null) {

                throw new IllegalArgumentException(
                        "Invalid meal-plan document.");
            }

            Map<WeekKey, MealPlanSnapshot> plans =
                    new LinkedHashMap<>();

            for (MealPlanJson storedPlan : store.plans()) {
                MealPlanSnapshot snapshot = decode(storedPlan);
                WeekKey key = snapshot.plan().getWeekKey();

                if (plans.putIfAbsent(key, snapshot) != null) {
                    throw new IllegalArgumentException(
                            "Duplicate weekly plan in storage.");
                }
            }

            return plans;

        } catch (JsonParseException
                 | IllegalArgumentException
                 | DateTimeException exception) {

            throw new MealPlanStorageException(
                    "The meal-plan file contains invalid data.",
                    exception);
        }
    }

    private MealPlanSnapshot decode(MealPlanJson stored) {
        if (stored == null
                || stored.userId() == null
                || stored.weekStart() == null
                || stored.revision() == null
                || stored.revision() < 1
                || stored.dinners() == null) {

            throw new IllegalArgumentException(
                    "Incomplete stored meal plan.");
        }

        WeekKey key = new WeekKey(
                new UserId(stored.userId()),
                LocalDate.parse(stored.weekStart()));

        Map<LocalDate, DinnerSlot> dinners = new HashMap<>();

        for (DinnerSlotJson storedDinner : stored.dinners()) {
            if (storedDinner == null
                    || storedDinner.date() == null
                    || storedDinner.recipeProvider() == null
                    || storedDinner.recipeId() == null
                    || storedDinner.numServings() == null) {

                throw new IllegalArgumentException(
                        "Incomplete stored dinner.");
            }

            LocalDate date =
                    LocalDate.parse(storedDinner.date());

            DinnerSlot dinner = new DinnerSlot(
                    new RecipeKey(
                            storedDinner.recipeProvider(),
                            storedDinner.recipeId()),
                    storedDinner.numServings());

            if (dinners.putIfAbsent(date, dinner) != null) {
                throw new IllegalArgumentException(
                        "Duplicate dinner date in storage.");
            }
        }

        // MealPlan validates that every date belongs to its week.
        MealPlan plan = new MealPlan(key, dinners);

        return new MealPlanSnapshot(
                plan, stored.revision());
    }

    private MealPlanJson encode(MealPlanSnapshot snapshot) {
        MealPlan plan = snapshot.plan();

        List<DinnerSlotJson> dinners =
                plan.getDinners().entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(entry -> new DinnerSlotJson(
                                entry.getKey().toString(),
                                entry.getValue().recipeKey().getProvider(),
                                entry.getValue().recipeKey().getId(),
                                entry.getValue().numServings()))
                        .toList();

        return new MealPlanJson(
                plan.getWeekKey().userId().value(),
                plan.getWeekKey().weekStart().toString(),
                snapshot.revision(),
                dinners);
    }

    private static void writeAtomically(
            Path target,
            String json) throws IOException {

        Path temporary = Files.createTempFile(
                target.getParent(),
                ".meal-plan-",
                ".tmp");

        boolean moved = false;

        try {
            Files.writeString(
                    temporary,
                    json,
                    StandardCharsets.UTF_8);

            Files.move(
                    temporary,
                    target,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);

            moved = true;

        } finally {
            if (!moved) {
                try {
                    Files.deleteIfExists(temporary);
                } catch (IOException exception) {
                    LOGGER.log(
                            System.Logger.Level.WARNING,
                            "Could not remove temporary meal-plan file.",
                            exception);
                }
            }
        }
    }

    private static void closeQuietly(AutoCloseable resource) {
        if (resource == null) {
            return;
        }

        try {
            resource.close();
        } catch (Exception exception) {
            LOGGER.log(
                    System.Logger.Level.WARNING,
                    "Could not close meal-plan storage resource.",
                    exception);
        }
    }
}