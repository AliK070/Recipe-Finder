package com.recipefinder.usecases.search.getrecipedetails;

import com.recipefinder.entities.RecipeKey;

public final class GetRecipeDetailsInputData {
    private final RecipeKey key;

    public GetRecipeDetailsInputData(RecipeKey key) { this.key = key; }

    public GetRecipeDetailsInputData(String provider, String id) {
        this(new RecipeKey(provider, id));
    }

    public RecipeKey getKey() { return key; }
    public String getProvider() { return key == null ? null : key.getProvider(); }
    public String getId() { return key == null ? null : key.getId(); }
}
