package com.recipefinder.usecases.preferences.loadpreferences;

/** Entry point for loading saved food preferences. */
public interface LoadPreferencesInputBoundary {
    void execute(LoadPreferencesInputData inputData);
}
