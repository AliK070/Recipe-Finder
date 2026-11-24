package com.recipefinder.interfaces;

import com.recipefinder.entities.RecipeKey;
import com.recipefinder.entities.UserId;

/** Favorites information required when assigning a dinner. */
@FunctionalInterface
public interface SavedRecipeLookup {
    boolean isSaved(UserId userId, RecipeKey recipeKey)
            throws MealPlanStorageException;
}