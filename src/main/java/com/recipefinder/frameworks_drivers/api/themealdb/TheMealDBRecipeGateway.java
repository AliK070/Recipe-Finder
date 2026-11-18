package com.recipefinder.frameworks_drivers.api.themealdb;

import com.recipefinder.entities.Recipe;
import com.recipefinder.entities.RecipeKey;
import com.recipefinder.entities.RecipeSummary;
import com.recipefinder.frameworks_drivers.api.themealdb.dto.TheMealDBRecipeDto;
import com.recipefinder.interfaces.RecipeGateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TheMealDBRecipeGateway implements RecipeGateway {
    private final TheMealDBClient client;
    private final TheMealDBMapper mapper;

    public TheMealDBRecipeGateway() {
        this(new TheMealDBClient(), new TheMealDBMapper());
    }

    public TheMealDBRecipeGateway(TheMealDBClient client, TheMealDBMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public List<RecipeSummary> searchRecipes(String query) throws IOException {
        List<RecipeSummary> summaries = new ArrayList<>();
        for (TheMealDBRecipeDto dto : client.search(query)) {
            RecipeSummary summary = mapper.toSummary(dto);
            if (summary != null) {
                summaries.add(summary);
            }
        }
        return Collections.unmodifiableList(summaries);
    }

    @Override
    public Recipe getRecipeDetails(RecipeKey key) throws IOException {
        if (key == null) {
            throw new IOException("Recipe key is missing");
        }
        if (!TheMealDBMapper.PROVIDER.equals(key.getProvider())) {
            throw new IOException("Recipe key belongs to an unsupported provider");
        }
        return client.lookup(key.getId()).stream()
                .findFirst()
                .map(mapper::toRecipe)
                .orElseThrow(() -> new IOException("Recipe not found"));
    }
}
