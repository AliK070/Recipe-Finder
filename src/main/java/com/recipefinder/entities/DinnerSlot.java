package com.recipefinder.entities;

import java.util.Objects;

/** A recipe and its serving count for one planned dinner. */
public record DinnerSlot(RecipeKey recipeKey, int numServings) {

    public static final int MIN_SERVINGS = 1;
    public static final int MAX_SERVINGS = 12;

    public DinnerSlot {
        Objects.requireNonNull(recipeKey, "recipeKey");

        if (numServings < MIN_SERVINGS || numServings > MAX_SERVINGS) {
            throw new IllegalArgumentException("Serving size must be between 1 and 12 servings.");
        }
    }
}
