package com.recipefinder.usecases.preferences.loadpreferences;

import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.UserId;

/** Result of loading a user's saved food preferences. */
public record LoadPreferencesOutputData(
        UserId userId,
        FoodPreferences preferences,
        boolean success,
        String message) {

    public static LoadPreferencesOutputData success(UserId userId, FoodPreferences preferences) {
        return new LoadPreferencesOutputData(userId, preferences, true, "Preferences loaded successfully.");
    }

    public static LoadPreferencesOutputData failure(UserId userId, String message) {
        return new LoadPreferencesOutputData(userId, null, false, message);
    }
}
