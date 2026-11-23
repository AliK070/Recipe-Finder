package com.recipefinder.frameworks_drivers.api.themealdb;

import com.recipefinder.entities.Ingredient;
import com.recipefinder.entities.Recipe;
import com.recipefinder.entities.RecipeKey;
import com.recipefinder.entities.RecipeSummary;
import com.recipefinder.frameworks_drivers.api.themealdb.dto.TheMealDBRecipeDto;
import java.util.ArrayList;
import java.util.List;

public final class TheMealDBMapper {
    public static final String PROVIDER = "themealdb";
    private static final int MAX_INGREDIENTS = 20;

    public RecipeSummary toSummary(TheMealDBRecipeDto dto) {
        if (dto == null) return null;
        return new RecipeSummary(
                new RecipeKey(PROVIDER, dto.getIdMeal()),
                dto.getStrMeal(), dto.getStrCategory(), dto.getStrArea(), dto.getStrMealThumb());
    }

    public Recipe toRecipe(TheMealDBRecipeDto dto) {
        if (dto == null) return null;
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 1; i <= MAX_INGREDIENTS; i++) {
            String name = trim(dto.getIngredient(i));
            if (!name.isEmpty()) {
                ingredients.add(new Ingredient(name, trim(dto.getMeasure(i))));
            }
        }
        return new Recipe(new RecipeKey(PROVIDER, dto.getIdMeal()), dto.getStrMeal(),
                dto.getStrInstructions(), ingredients, dto.getStrMealThumb(),
                dto.getStrCategory(), dto.getStrArea());
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
