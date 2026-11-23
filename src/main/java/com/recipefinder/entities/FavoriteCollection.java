package com.recipefinder.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** The recipe IDs saved by one account. */
public record FavoriteCollection(UserId userId, Set<RecipeKey> recipeKeys) {
    public FavoriteCollection {
        Objects.requireNonNull(userId, "userId");
        recipeKeys = Set.copyOf(Objects.requireNonNull(recipeKeys, "recipeKeys"));
    }

    public boolean contains(RecipeKey recipeKey) {
        return recipeKeys.contains(Objects.requireNonNull(recipeKey, "recipeKey"));
    }

    public FavoriteCollection add(RecipeKey recipeKey) {
        Set<RecipeKey> updatedKeys = new HashSet<>(recipeKeys);
        updatedKeys.add(Objects.requireNonNull(recipeKey, "recipeKey"));
        return new FavoriteCollection(userId, updatedKeys);
    }

    public FavoriteCollection remove(RecipeKey recipeKey) {
        Set<RecipeKey> updatedKeys = new HashSet<>(recipeKeys);
        updatedKeys.remove(Objects.requireNonNull(recipeKey, "recipeKey"));
        return new FavoriteCollection(userId, updatedKeys);
    }
}
