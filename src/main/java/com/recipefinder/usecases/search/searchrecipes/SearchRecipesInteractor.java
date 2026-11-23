package com.recipefinder.usecases.search.searchrecipes;

import com.recipefinder.interfaces.RecipeGateway;
import java.io.IOException;

public final class SearchRecipesInteractor implements SearchRecipesInputBoundary {
    private final RecipeGateway gateway;
    private final SearchRecipesOutputBoundary presenter;

    public SearchRecipesInteractor(RecipeGateway gateway, SearchRecipesOutputBoundary presenter) {
        this.gateway = gateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchRecipesInputData inputData) {
        if (inputData == null || inputData.getQuery() == null || inputData.getQuery().trim().isEmpty()) {
            presenter.prepareFailView("Enter a recipe name to search.");
            return;
        }

        try {
            presenter.prepareSuccessView(new SearchRecipesOutputData(
                    gateway.searchRecipes(inputData.getQuery().trim())));
        } catch (IOException | RuntimeException exception) {
            presenter.prepareFailView("Unable to search recipes. Please try again.");
        }
    }

    public void execute(String query) {
        execute(new SearchRecipesInputData(query));
    }
}
