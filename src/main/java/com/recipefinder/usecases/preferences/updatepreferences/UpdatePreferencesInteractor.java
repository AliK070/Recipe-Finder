package com.recipefinder.usecases.preferences.updatepreferences;

import com.recipefinder.interfaces.AccountRepository;
import com.recipefinder.interfaces.PreferencesRepository;
import java.util.Objects;

/** Updates preferences for an existing account and persists them through a repository. */
public final class UpdatePreferencesInteractor implements UpdatePreferencesInputBoundary {
    private final AccountRepository accountRepository;
    private final PreferencesRepository preferencesRepository;
    private final UpdatePreferencesOutputBoundary presenter;

    public UpdatePreferencesInteractor(
            AccountRepository accountRepository,
            PreferencesRepository preferencesRepository,
            UpdatePreferencesOutputBoundary presenter) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
        this.preferencesRepository = Objects.requireNonNull(preferencesRepository, "preferencesRepository");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(UpdatePreferencesInputData inputData) {
        if (inputData == null || inputData.userId() == null || inputData.preferences() == null) {
            presenter.present(UpdatePreferencesOutputData.failure(
                    inputData == null ? null : inputData.userId(),
                    "A user ID and preferences are required."));
            return;
        }

        try {
            if (accountRepository.findByUserId(inputData.userId()).isEmpty()) {
                presenter.present(UpdatePreferencesOutputData.failure(
                        inputData.userId(), "Account was not found."));
                return;
            }
        } catch (RuntimeException exception) {
            presenter.present(UpdatePreferencesOutputData.failure(
                    inputData.userId(), "Could not check account storage. Please try again."));
            return;
        }

        try {
            preferencesRepository.save(inputData.userId(), inputData.preferences());
        } catch (RuntimeException exception) {
            presenter.present(UpdatePreferencesOutputData.failure(
                    inputData.userId(), "Could not save preferences. Please try again."));
            return;
        }

        presenter.present(UpdatePreferencesOutputData.success(inputData.userId(), inputData.preferences()));
    }
}
