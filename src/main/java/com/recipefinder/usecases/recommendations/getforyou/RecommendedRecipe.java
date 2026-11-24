package com.recipefinder.usecases.recommendations.getforyou;

import com.recipefinder.entities.RecipeKey;

import java.util.Objects;

/** One ranked For You result, with the reason it was recommended. */
public record RecommendedRecipe(
        RecipeKey key,
        String name,
        String category,
        String cuisine,
        String thumbnailUrl,
        int score,
        String reason) {

    public RecommendedRecipe {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(reason, "reason");
    }
}