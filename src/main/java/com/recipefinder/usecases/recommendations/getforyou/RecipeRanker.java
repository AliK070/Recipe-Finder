package com.recipefinder.usecases.recommendations.getforyou;

import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.Recipe;
import com.recipefinder.entities.RecipeKey;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Turns candidate recipes into an ordered For You list:
 * filter out unsafe recipes, score the rest, sort them, then add variety.
 */
public final class RecipeRanker {

    private static final int CUISINE_POINTS = 2;
    private static final int CATEGORY_POINTS = 1;

    private final RecipeEligibilityPolicy eligibilityPolicy;

    public RecipeRanker(RecipeEligibilityPolicy eligibilityPolicy) {
        this.eligibilityPolicy = Objects.requireNonNull(eligibilityPolicy, "eligibilityPolicy");
    }

    /**
     * Returns the eligible recipes, best match first.
     *
     * @param candidates the recipes to choose from
     * @param preferences the user's saved food preferences
     * @return the ranked recommendations, possibly empty
     */
    public List<RecommendedRecipe> rank(List<Recipe> candidates, FoodPreferences preferences) {
        List<RecommendedRecipe> scored = new ArrayList<>();
        Set<RecipeKey> seen = new HashSet<>();
        for (Recipe recipe : candidates) {
            if (seen.add(recipe.getKey()) && eligibilityPolicy.isEligible(recipe, preferences)) {
                scored.add(score(recipe, preferences));
            }
        }
        scored.sort(Comparator.comparingInt(RecommendedRecipe::score).reversed()
                .thenComparing(RecommendedRecipe::name, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(r -> r.key().toString()));
        return interleaveTies(scored);
    }

    private RecommendedRecipe score(Recipe recipe, FoodPreferences preferences) {
        boolean cuisineMatch = containsIgnoreCase(preferences.preferredCuisines(), recipe.getCuisine());
        boolean categoryMatch = containsIgnoreCase(preferences.preferredCategories(), recipe.getCategory());

        int score = 0;
        if (cuisineMatch) {
            score += CUISINE_POINTS;
        }
        if (categoryMatch) {
            score += CATEGORY_POINTS;
        }
        return new RecommendedRecipe(recipe.getKey(), recipe.getName(), recipe.getCategory(),
                recipe.getCuisine(), recipe.getThumbnailUrl(), score,
                reason(cuisineMatch, categoryMatch));
    }

    private String reason(boolean cuisineMatch, boolean categoryMatch) {
        if (cuisineMatch && categoryMatch) {
            return "Matches your cuisine and category";
        }
        if (cuisineMatch) {
            return "Matches your preferred cuisine";
        }
        if (categoryMatch) {
            return "Matches your preferred category";
        }
        return "Fits your saved settings";
    }

    /**
     * Among recipes with the same score, avoids putting two of the same category
     * next to each other. Higher scores always stay above lower ones.
     */
    private List<RecommendedRecipe> interleaveTies(List<RecommendedRecipe> sorted) {
        List<RecommendedRecipe> remaining = new ArrayList<>(sorted);
        List<RecommendedRecipe> result = new ArrayList<>();
        String lastCategory = null;
        while (!remaining.isEmpty()) {
            int topScore = remaining.get(0).score();
            int pick = 0;
            for (int i = 0; i < remaining.size() && remaining.get(i).score() == topScore; i++) {
                if (!remaining.get(i).category().equalsIgnoreCase(lastCategory)) {
                    pick = i;
                    break;
                }
            }
            RecommendedRecipe next = remaining.remove(pick);
            result.add(next);
            lastCategory = next.category();
        }
        return result;
    }

    private static boolean containsIgnoreCase(Set<String> values, String target) {
        for (String value : values) {
            if (value.trim().equalsIgnoreCase(target.trim())) {
                return true;
            }
        }
        return false;
    }
}