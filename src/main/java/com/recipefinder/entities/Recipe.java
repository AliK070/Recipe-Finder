package com.recipefinder.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Recipe {
    private final RecipeKey key;
    private final String name;
    private final String instructions;
    private final List<Ingredient> ingredients;
    private final String thumbnailUrl;
    private final String category;
    private final String cuisine;

    public Recipe(RecipeKey key, String name, String instructions, List<Ingredient> ingredients,
                  String thumbnailUrl, String category, String cuisine) {
        this.key = Objects.requireNonNull(key, "key must not be null");
        this.name = name == null ? "" : name;
        this.instructions = instructions == null ? "" : instructions;
        this.ingredients = Collections.unmodifiableList(new ArrayList<>(
                ingredients == null ? Collections.emptyList() : ingredients));
        this.thumbnailUrl = thumbnailUrl == null ? "" : thumbnailUrl;
        this.category = category == null ? "" : category;
        this.cuisine = cuisine == null ? "" : cuisine;
    }

    public RecipeKey getKey() { return key; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getCategory() { return category; }
    public String getCuisine() { return cuisine; }
}
