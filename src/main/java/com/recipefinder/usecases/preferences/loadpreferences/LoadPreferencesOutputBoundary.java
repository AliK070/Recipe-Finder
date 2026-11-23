package com.recipefinder.usecases.preferences.loadpreferences;

/** Receives the result of loading saved food preferences. */
public interface LoadPreferencesOutputBoundary {
    void present(LoadPreferencesOutputData outputData);
}
