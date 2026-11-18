package com.recipefinder.entities;

import java.util.Objects;

/** A named ingredient used by a recipe or excluded in food preferences. */
public record Ingredient(String name) {
    public Ingredient {
        Objects.requireNonNull(name, "name");
        // Trim whitespace to ensure consistent ingredient names,
        // even though users do not manually enter ingredients in our programme.
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Ingredient name cannot be blank");
        }
    }
}
