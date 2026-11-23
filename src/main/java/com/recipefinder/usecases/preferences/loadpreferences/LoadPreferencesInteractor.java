package com.recipefinder.usecases.preferences.loadpreferences;

import com.recipefinder.interfaces.PreferencesRepository;
import java.util.Objects;

/** Loads saved food preferences without depending on a storage implementation. */
public final class LoadPreferencesInteractor implements LoadPreferencesInputBoundary {
    private final PreferencesRepository preferencesRepository;
    private final LoadPreferencesOutputBoundary presenter;

    public LoadPreferencesInteractor(
            PreferencesRepository preferencesRepository,
            LoadPreferencesOutputBoundary presenter) {
        this.preferencesRepository = Objects.requireNonNull(preferencesRepository, "preferencesRepository");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(LoadPreferencesInputData inputData) {
        if (inputData == null || inputData.userId() == null) {
            presenter.present(LoadPreferencesOutputData.failure(null, "A user ID is required to load preferences."));
            return;
        }

        try {
            var preferences = preferencesRepository.findByUserId(inputData.userId());
            if (preferences.isEmpty()) {
                presenter.present(LoadPreferencesOutputData.failure(
                        inputData.userId(), "No saved preferences were found for this account."));
                return;
            }
            presenter.present(LoadPreferencesOutputData.success(inputData.userId(), preferences.get()));
        } catch (RuntimeException exception) {
            presenter.present(LoadPreferencesOutputData.failure(
                    inputData.userId(), "Could not load preferences. Please try again."));
        }
    }
}
