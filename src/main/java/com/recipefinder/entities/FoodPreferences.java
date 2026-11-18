package com.recipefinder.entities;

import java.util.Objects;
import java.util.Set;

/** Food choices used to filter and personalize recipes for an account. */
public record FoodPreferences(
        DietaryType dietaryType,
        Set<Allergen> excludedAllergens,
        Set<Ingredient> excludedIngredients,
        Set<String> preferredAreas,
        Set<String> preferredCategories) {

    public FoodPreferences {
        Objects.requireNonNull(dietaryType, "dietaryType");
        excludedAllergens = Set.copyOf(
                Objects.requireNonNull(excludedAllergens, "excludedAllergens"));
        excludedIngredients = Set.copyOf(
                Objects.requireNonNull(excludedIngredients, "excludedIngredients"));
        preferredAreas = Set.copyOf(
                Objects.requireNonNull(preferredAreas, "preferredAreas"));
        preferredCategories = Set.copyOf(
                Objects.requireNonNull(preferredCategories, "preferredCategories"));
    }

    public static FoodPreferences defaults() {
        return new FoodPreferences(
                DietaryType.NONE,
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of()
        );
    }
}
