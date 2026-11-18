package com.recipefinder.usecases.search.getrecipedetails;

import com.recipefinder.entities.Recipe;

public final class GetRecipeDetailsOutputData {
    private final Recipe recipe;
    private final String errorMessage;

    public GetRecipeDetailsOutputData(Recipe recipe) { this(recipe, null); }

    public GetRecipeDetailsOutputData(Recipe recipe, String errorMessage) {
        this.recipe = recipe;
        this.errorMessage = errorMessage;
    }

    public Recipe getRecipe() { return recipe; }
    public String getErrorMessage() { return errorMessage; }
    public boolean isSuccess() { return errorMessage == null; }
}
