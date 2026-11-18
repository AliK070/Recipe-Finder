package com.recipefinder.entities;

import java.util.Objects;

/** Identifies a recipe returned by the recipe provider. */
public record RecipeKey(String value) {
    public RecipeKey {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Recipe ID cannot be blank");
        }
    }
}
