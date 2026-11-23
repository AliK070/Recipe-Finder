package com.recipefinder.usecases.search.searchrecipes;

import com.recipefinder.entities.RecipeSummary;
import java.util.Collections;
import java.util.List;

public final class SearchRecipesOutputData {
    private final List<RecipeSummary> recipes;
    private final String errorMessage;

    public SearchRecipesOutputData(List<RecipeSummary> recipes) {
        this(recipes, null);
    }

    public SearchRecipesOutputData(List<RecipeSummary> recipes, String errorMessage) {
        this.recipes = recipes == null ? Collections.emptyList() : Collections.unmodifiableList(recipes);
        this.errorMessage = errorMessage;
    }

    public List<RecipeSummary> getRecipes() { return recipes; }
    public String getErrorMessage() { return errorMessage; }
    public boolean isSuccess() { return errorMessage == null; }
}
