package com.recipefinder.usecases.preferences.updatepreferences;

/** Receives the result of updating saved food preferences. */
public interface UpdatePreferencesOutputBoundary {
    void present(UpdatePreferencesOutputData outputData);
}
