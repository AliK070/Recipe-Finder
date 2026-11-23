package com.recipefinder.usecases.account.createaccount;

import com.recipefinder.entities.Account;
import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.UserId;
import com.recipefinder.interfaces.AccountRepository;
import java.util.Objects;
import java.util.UUID;

/** Creates an account after validating its username and password. */
public final class CreateAccountInteractor implements CreateAccountInputBoundary {
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final AccountRepository accountRepository;
    private final CreateAccountOutputBoundary presenter;

    public CreateAccountInteractor(
            AccountRepository accountRepository,
            CreateAccountOutputBoundary presenter) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(CreateAccountInputData inputData) {
        if (inputData == null) {
            presenter.present(CreateAccountOutputData.failure("Account information is required."));
            return;
        }

        String username = inputData.username() == null ? "" : inputData.username().trim();
        String password = inputData.password();
        if (username.isEmpty() || password == null || password.isBlank()) {
            presenter.present(CreateAccountOutputData.failure("Username and password are required."));
            return;
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            presenter.present(CreateAccountOutputData.failure(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long."));
            return;
        }

        try {
            if (accountRepository.findByUsername(username).isPresent()) {
                presenter.present(CreateAccountOutputData.failure("That username is already in use."));
                return;
            }
        } catch (RuntimeException exception) {
            presenter.present(CreateAccountOutputData.failure("Could not check account storage. Please try again."));
            return;
        }

        UserId userId = new UserId(UUID.randomUUID().toString());
        Account account = new Account(userId, username, password, FoodPreferences.defaults());
        try {
            accountRepository.save(account);
        } catch (RuntimeException exception) {
            presenter.present(CreateAccountOutputData.failure("Could not save the account. Please try again."));
            return;
        }

        presenter.present(CreateAccountOutputData.success(userId));
    }
}
