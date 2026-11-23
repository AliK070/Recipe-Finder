package com.recipefinder.interfaces;
import com.recipefinder.entities.Recipe;

import java.io.IOException;
import java.util.List;

/**
 * Supplies full recipes (including ingredients) for the For You page to filter and rank.
 * Kept separate from RecipeGateway so Search and For You each depend only on
 * what they use (Interface Segregation Principle).
 */
public interface RecipeCandidateGateway{
    /**
     * Returns up to limit recipes to consider for recommendations.
     *
     * @param limit the maximum number of recipes to return
     * @return the candidate recipes, possibly empty
     * @throws IOException if the recipe provider cannot be reached
     */
    List<Recipe> findCandidates(int limit) throws IOException;
}
