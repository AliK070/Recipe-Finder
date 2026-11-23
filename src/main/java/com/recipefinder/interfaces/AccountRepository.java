package com.recipefinder.interfaces;

import com.recipefinder.entities.Account;
import com.recipefinder.entities.UserId;
import java.util.Optional;

/** Persistence operations required by account and profile use cases. */
public interface AccountRepository {
    Optional<Account> findByUserId(UserId userId);

    Optional<Account> findByUsername(String username);

    /** Saves a new account; implementations should reject duplicate usernames. */
    void save(Account account);
}
