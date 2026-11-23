package com.recipefinder.entities;

import java.util.Objects;

public final class RecipeSummary {
    private final RecipeKey key;
    private final String name;
    private final String category;
    private final String cuisine;
    private final String thumbnailUrl;

    public RecipeSummary(RecipeKey key, String name, String category, String cuisine, String thumbnailUrl) {
        this.key = Objects.requireNonNull(key, "key must not be null");
        this.name = name == null ? "" : name;
        this.category = category == null ? "" : category;
        this.cuisine = cuisine == null ? "" : cuisine;
        this.thumbnailUrl = thumbnailUrl == null ? "" : thumbnailUrl;
    }

    public RecipeKey getKey() { return key; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getCuisine() { return cuisine; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}
