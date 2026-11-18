package com.recipefinder.entities;

import java.util.Objects;

/** The compact recipe information shown in search and favorites lists. */
public record RecipeSummary(RecipeId recipeId, String title, String thumbnailUrl) {
    public RecipeSummary {
        Objects.requireNonNull(recipeId, "recipeId");
        Objects.requireNonNull(title, "title");
        title = title.trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Recipe title cannot be blank");
        }
        if (thumbnailUrl == null) {
            thumbnailUrl = "";
        } else {
            thumbnailUrl = thumbnailUrl.trim();
        }
    }
}
