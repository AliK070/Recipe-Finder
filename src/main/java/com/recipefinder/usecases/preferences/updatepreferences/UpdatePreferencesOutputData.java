package com.recipefinder.usecases.preferences.updatepreferences;

import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.UserId;

/** Result of attempting to update a user's food preferences. */
public record UpdatePreferencesOutputData(
        UserId userId,
        FoodPreferences preferences,
        boolean success,
        String message) {

    public static UpdatePreferencesOutputData success(UserId userId, FoodPreferences preferences) {
        return new UpdatePreferencesOutputData(userId, preferences, true, "Preferences updated successfully.");
    }

    public static UpdatePreferencesOutputData failure(UserId userId, String message) {
        return new UpdatePreferencesOutputData(userId, null, false, message);
    }
}
