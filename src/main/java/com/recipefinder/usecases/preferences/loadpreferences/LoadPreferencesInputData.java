package com.recipefinder.usecases.preferences.loadpreferences;

import com.recipefinder.entities.UserId;

/** Input identifying whose saved preferences should be loaded. */
public record LoadPreferencesInputData(UserId userId) {}
