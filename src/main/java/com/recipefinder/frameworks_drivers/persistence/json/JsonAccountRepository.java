package com.recipefinder.frameworks_drivers.persistence.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipefinder.entities.Account;
import com.recipefinder.entities.UserId;
import com.recipefinder.interfaces.AccountRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/** Stores accounts as a JSON array in the configured file. */
public final class JsonAccountRepository implements AccountRepository {
    private static final TypeReference<List<Account>> ACCOUNT_LIST_TYPE = new TypeReference<>() {};

    private final Path file;
    private final ObjectMapper mapper;
    private final AtomicJsonWriter writer;

    public JsonAccountRepository(Path file) {
        this(file, new ObjectMapper(), new AtomicJsonWriter());
    }

    JsonAccountRepository(Path file, ObjectMapper mapper, AtomicJsonWriter writer) {
        this.file = Objects.requireNonNull(file, "file");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.writer = Objects.requireNonNull(writer, "writer");
    }

    @Override
    public synchronized Optional<Account> findByUserId(UserId userId) {
        Objects.requireNonNull(userId, "userId");
        return readAccounts().stream()
                .filter(account -> account.userId().equals(userId))
                .findFirst();
    }

    @Override
    public synchronized Optional<Account> findByUsername(String username) {
        String normalizedUsername = normalizeUsername(username);
        return readAccounts().stream()
                .filter(account -> normalizeUsername(account.username()).equals(normalizedUsername))
                .findFirst();
    }

    @Override
    public synchronized void save(Account account) {
        Objects.requireNonNull(account, "account");
        List<Account> accounts = readAccounts();
        boolean duplicateUsername = accounts.stream()
                .anyMatch(existing -> normalizeUsername(existing.username())
                        .equals(normalizeUsername(account.username())));
        if (duplicateUsername) {
            throw new IllegalArgumentException("An account with that username already exists.");
        }
        boolean duplicateId = accounts.stream()
                .anyMatch(existing -> existing.userId().equals(account.userId()));
        if (duplicateId) {
            throw new IllegalArgumentException("An account with that user ID already exists.");
        }

        accounts.add(account);
        writeAccounts(accounts);
    }

    private List<Account> readAccounts() {
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        try {
            if (Files.size(file) == 0) {
                return new ArrayList<>();
            }
            List<Account> accounts = mapper.readValue(file.toFile(), ACCOUNT_LIST_TYPE);
            return accounts == null ? new ArrayList<>() : new ArrayList<>(accounts);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not read accounts from " + file, exception);
        }
    }

    private void writeAccounts(List<Account> accounts) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accounts);
            writer.write(file, json + System.lineSeparator());
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not convert accounts to JSON.", exception);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not save accounts to " + file, exception);
        }
    }

    private static String normalizeUsername(String username) {
        Objects.requireNonNull(username, "username");
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
