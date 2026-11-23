package com.recipefinder.frameworks_drivers.api.themealdb.dto;

import java.util.HashMap;
import java.util.Map;

public final class TheMealDBRecipeDto {
    private String idMeal;
    private String strMeal;
    private String strInstructions;
    private String strMealThumb;
    private String strCategory;
    private String strArea;
    private final Map<Integer, String> ingredients = new HashMap<>();
    private final Map<Integer, String> measures = new HashMap<>();

    public String getIdMeal() { return idMeal; }
    public void setIdMeal(String idMeal) { this.idMeal = idMeal; }
    public String getStrMeal() { return strMeal; }
    public void setStrMeal(String strMeal) { this.strMeal = strMeal; }
    public String getStrInstructions() { return strInstructions; }
    public void setStrInstructions(String strInstructions) { this.strInstructions = strInstructions; }
    public String getStrMealThumb() { return strMealThumb; }
    public void setStrMealThumb(String strMealThumb) { this.strMealThumb = strMealThumb; }
    public String getStrCategory() { return strCategory; }
    public void setStrCategory(String strCategory) { this.strCategory = strCategory; }
    public String getStrArea() { return strArea; }
    public void setStrArea(String strArea) { this.strArea = strArea; }

    public String getIngredient(int number) {
        return ingredients.get(number);
    }

    public void setIngredient(int number, String value) {
        ingredients.put(number, value);
    }

    public String getMeasure(int number) {
        return measures.get(number);
    }

    public void setMeasure(int number, String value) {
        measures.put(number, value);
    }
}
