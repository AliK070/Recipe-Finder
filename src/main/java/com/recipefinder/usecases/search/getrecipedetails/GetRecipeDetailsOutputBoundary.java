package com.recipefinder.usecases.search.getrecipedetails;

public interface GetRecipeDetailsOutputBoundary {
    void prepareSuccessView(GetRecipeDetailsOutputData outputData);
    void prepareFailView(String errorMessage);
}
