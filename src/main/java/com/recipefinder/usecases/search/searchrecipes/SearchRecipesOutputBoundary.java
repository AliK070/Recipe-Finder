package com.recipefinder.usecases.search.searchrecipes;

public interface SearchRecipesOutputBoundary {
    void prepareSuccessView(SearchRecipesOutputData outputData);
    void prepareFailView(String errorMessage);
}
