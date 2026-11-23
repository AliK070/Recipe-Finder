package com.recipefinder.usecases.preferences.updatepreferences;

import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.UserId;

/** Preferences submitted for a specific account. */
public record UpdatePreferencesInputData(UserId userId, FoodPreferences preferences) {}
