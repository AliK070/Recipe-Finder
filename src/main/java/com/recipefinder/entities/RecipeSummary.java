
package com.recipefinder.entities;

import java.util.Objects;

/** The compact recipe information shown in search and favorites lists. */
public final class RecipeSummary {
    private final RecipeKey key;
    private final String name;
    private final String category;
    private final String cuisine;
    private final String thumbnailUrl;

    public RecipeSummary(RecipeKey key, String name, String category,
                         String cuisine, String thumbnailUrl) {
        this.key = Objects.requireNonNull(key, "key must not be null");

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe title cannot be blank");
        }

        this.name = name.trim();
        this.category = category == null ? "" : category.trim();
        this.cuisine = cuisine == null ? "" : cuisine.trim();
        this.thumbnailUrl = thumbnailUrl == null ? "" : thumbnailUrl.trim();
    }

    public RecipeKey getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
