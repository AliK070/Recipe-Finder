package com.recipefinder.frameworks_drivers.api.themealdb.dto;

import java.util.Collections;
import java.util.List;

public final class TheMealDBSearchResponse {
    private List<TheMealDBRecipeDto> meals;

    public List<TheMealDBRecipeDto> getMeals() { return meals; }
    public void setMeals(List<TheMealDBRecipeDto> meals) {
        this.meals = meals == null ? Collections.emptyList() : meals;
    }
}
