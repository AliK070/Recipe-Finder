package com.recipefinder.interfaces;
import com.recipefinder.entities.Recipe;

import java.io.IOException;
import java.util.List;

/**
 * Supplies full recipes (including ingredients) for the For You page to filter and rank.
 */
public interface RecipeCandidateGateway{
    List<Recipe> findCandidates(int limit) throws IOException;
}
