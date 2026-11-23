package com.recipefinder.interfaces;

import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.UserId;
import java.util.Optional;

/** Persistence operations for a user's food preferences. */
public interface PreferencesRepository {
    Optional<FoodPreferences> findByUserId(UserId userId);

    /** Creates or replaces the saved preferences for the given user. */
    void save(UserId userId, FoodPreferences preferences);
}
