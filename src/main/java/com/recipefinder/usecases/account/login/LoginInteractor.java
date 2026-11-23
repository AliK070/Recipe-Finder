package com.recipefinder.usecases.account.login;

import com.recipefinder.entities.Account;
import com.recipefinder.interfaces.AccountRepository;
import com.recipefinder.interfaces.SessionManager;
import java.util.Objects;
import java.util.Optional;

/** Checks account credentials and starts a session for a valid account. */
public final class LoginInteractor implements LoginInputBoundary {
    private final AccountRepository accountRepository;
    private final SessionManager sessionManager;
    private final LoginOutputBoundary presenter;

    public LoginInteractor(
            AccountRepository accountRepository,
            SessionManager sessionManager,
            LoginOutputBoundary presenter) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
        this.sessionManager = Objects.requireNonNull(sessionManager, "sessionManager");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(LoginInputData inputData) {
        if (inputData == null
                || inputData.username() == null
                || inputData.username().isBlank()
                || inputData.password() == null
                || inputData.password().isBlank()) {
            presenter.present(LoginOutputData.failure("Username and password are required."));
            return;
        }

        final Optional<Account> account;
        try {
            account = accountRepository.findByUsername(inputData.username().trim());
        } catch (RuntimeException exception) {
            presenter.present(LoginOutputData.failure("Could not access account storage. Please try again."));
            return;
        }

        if (account.isEmpty() || !account.get().password().equals(inputData.password())) {
            presenter.present(LoginOutputData.failure("Username or password is incorrect."));
            return;
        }

        try {
            sessionManager.startSession(account.get().userId());
        } catch (RuntimeException exception) {
            presenter.present(LoginOutputData.failure("Could not start a session. Please try again."));
            return;
        }

        presenter.present(LoginOutputData.success(account.get().userId()));
    }
}
