package com.recipefinder.usecases.search.getrecipedetails;

import com.recipefinder.entities.RecipeKey;
import com.recipefinder.interfaces.RecipeGateway;
import java.io.IOException;

public final class GetRecipeDetailsInteractor implements GetRecipeDetailsInputBoundary {
    private final RecipeGateway gateway;
    private final GetRecipeDetailsOutputBoundary presenter;

    public GetRecipeDetailsInteractor(RecipeGateway gateway, GetRecipeDetailsOutputBoundary presenter) {
        this.gateway = gateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetRecipeDetailsInputData inputData) {
        if (inputData == null || inputData.getKey() == null) {
            presenter.prepareFailView("A valid recipe is required.");
            return;
        }

        try {
            presenter.prepareSuccessView(new GetRecipeDetailsOutputData(
                    gateway.getRecipeDetails(inputData.getKey())));
        } catch (IOException | RuntimeException exception) {
            presenter.prepareFailView("Unable to load recipe details. Please try again.");
        }
    }

    public void execute(RecipeKey key) { execute(new GetRecipeDetailsInputData(key)); }
    public void execute(String provider, String id) { execute(new GetRecipeDetailsInputData(provider, id)); }
}
