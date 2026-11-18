package com.recipefinder.interfaces;

import com.recipefinder.entities.Recipe;
import com.recipefinder.entities.RecipeKey;
import com.recipefinder.entities.RecipeSummary;

import java.io.IOException;
import java.util.List;

public interface RecipeGateway {
    List<RecipeSummary> searchRecipes(String query) throws IOException;
    Recipe getRecipeDetails(RecipeKey key) throws IOException;

    default List<RecipeSummary> searchByName(String query) throws IOException {
        return searchRecipes(query);
    }

    default Recipe getRecipeDetails(String provider, String id) throws IOException {
        return getRecipeDetails(new RecipeKey(provider, id));
    }
}
