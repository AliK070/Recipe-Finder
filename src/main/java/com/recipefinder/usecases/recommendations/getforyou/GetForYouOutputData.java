package com.recipefinder.usecases.recommendations.getforyou;

import java.util.List;

/** The ranked For You recommendations, plus a message for the user (empty if none). */
public record GetForYouOutputData(List<RecommendedRecipe> recipes, String message) {

    public GetForYouOutputData {
        recipes = List.copyOf(recipes);
        message = message == null ? "" : message;
    }
}