package com.recipefinder.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** The recipe IDs saved by one account. */
public record FavoriteCollection(UserId userId, Set<RecipeId> recipeIds) {
    public FavoriteCollection {
        Objects.requireNonNull(userId, "userId");
        recipeIds = Set.copyOf(Objects.requireNonNull(recipeIds, "recipeIds"));
    }

    public boolean contains(RecipeId recipeId) {
        return recipeIds.contains(Objects.requireNonNull(recipeId, "recipeId"));
    }

    public FavoriteCollection add(RecipeId recipeId) {
        Set<RecipeId> updatedIds = new HashSet<>(recipeIds);
        updatedIds.add(Objects.requireNonNull(recipeId, "recipeId"));
        return new FavoriteCollection(userId, updatedIds);
    }

    public FavoriteCollection remove(RecipeId recipeId) {
        Set<RecipeId> updatedIds = new HashSet<>(recipeIds);
        updatedIds.remove(Objects.requireNonNull(recipeId, "recipeId"));
        return new FavoriteCollection(userId, updatedIds);
    }
}
