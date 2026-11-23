package com.recipefinder.frameworks_drivers.persistence.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipefinder.entities.Account;
import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.UserId;
import com.recipefinder.interfaces.PreferencesRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Reads and updates preferences stored inside each account in the accounts JSON file. */
public final class JsonPreferencesRepository implements PreferencesRepository {
    private static final TypeReference<List<Account>> ACCOUNT_LIST_TYPE = new TypeReference<>() {};

    private final Path accountsFile;
    private final ObjectMapper mapper;
    private final AtomicJsonWriter writer;

    /** Use the same accounts JSON path supplied to {@link JsonAccountRepository}. */
    public JsonPreferencesRepository(Path accountsFile) {
        this(accountsFile, new ObjectMapper(), new AtomicJsonWriter());
    }

    JsonPreferencesRepository(Path accountsFile, ObjectMapper mapper, AtomicJsonWriter writer) {
        this.accountsFile = Objects.requireNonNull(accountsFile, "accountsFile");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.writer = Objects.requireNonNull(writer, "writer");
    }

    @Override
    public synchronized Optional<FoodPreferences> findByUserId(UserId userId) {
        Objects.requireNonNull(userId, "userId");
        return readAccounts().stream()
                .filter(account -> account.userId().equals(userId))
                .map(Account::foodPreferences)
                .findFirst();
    }

    @Override
    public synchronized void save(UserId userId, FoodPreferences preferences) {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(preferences, "preferences");

        List<Account> accounts = readAccounts();
        int accountIndex = -1;
        for (int index = 0; index < accounts.size(); index++) {
            if (accounts.get(index).userId().equals(userId)) {
                accountIndex = index;
                break;
            }
        }
        if (accountIndex < 0) {
            throw new IllegalArgumentException("Cannot save preferences because the account was not found.");
        }

        Account account = accounts.get(accountIndex);
        accounts.set(accountIndex, new Account(
                account.userId(),
                account.username(),
                account.password(),
                preferences));
        writeAccounts(accounts);
    }

    private List<Account> readAccounts() {
        if (!Files.exists(accountsFile)) {
            return new ArrayList<>();
        }
        try {
            if (Files.size(accountsFile) == 0) {
                return new ArrayList<>();
            }
            List<Account> accounts = mapper.readValue(accountsFile.toFile(), ACCOUNT_LIST_TYPE);
            return accounts == null ? new ArrayList<>() : new ArrayList<>(accounts);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not read account preferences from " + accountsFile, exception);
        }
    }

    private void writeAccounts(List<Account> accounts) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accounts);
            writer.write(accountsFile, json + System.lineSeparator());
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not convert account preferences to JSON.", exception);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not save account preferences to " + accountsFile, exception);
        }
    }
}
