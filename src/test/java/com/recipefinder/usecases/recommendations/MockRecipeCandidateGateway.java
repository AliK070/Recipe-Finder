package com.recipefinder.usecases.recommendations;

import com.recipefinder.entities.Ingredient;
import com.recipefinder.entities.Recipe;
import com.recipefinder.entities.RecipeKey;
import com.recipefinder.interfaces.RecipeCandidateGateway;

import java.util.ArrayList;
import java.util.List;

/**
 * Test-only source of hand-made recipes, chosen to exercise every filtering rule
 * (meat, seafood, dairy, eggs, nuts, wheat, and a recipe with no ingredient list).
 */
public final class MockRecipeCandidateGateway implements RecipeCandidateGateway {

    private static final String PROVIDER = "mock";

    private final List<Recipe> recipes = List.of(
            recipe("1", "Chicken Alfredo", "Chicken", "Italian",
                    "Chicken Breast", "Penne", "Double Cream", "Parmesan"),
            recipe("2", "Chicken Curry", "Chicken", "Indian",
                    "Chicken Thighs", "Onion", "Garam Masala", "Coconut Milk"),
            recipe("3", "Beef Stew", "Beef", "British",
                    "Beef", "Carrots", "Potatoes", "Plain Flour"),
            recipe("4", "Salmon Teriyaki", "Seafood", "Japanese",
                    "Salmon", "Soy Sauce", "Honey", "Rice"),
            recipe("5", "Garlic Prawns", "Seafood", "Spanish",
                    "King Prawns", "Garlic", "Olive Oil"),
            recipe("6", "Vegetable Lasagne", "Vegetarian", "Italian",
                    "Lasagne Sheets", "Spinach", "Ricotta", "Tomato"),
            recipe("7", "Chickpea Salad", "Vegan", "Moroccan",
                    "Chickpeas", "Cucumber", "Lemon", "Olive Oil"),
            recipe("8", "Moussaka", "Vegetarian", "Greek",
                    "Eggplant", "Tomato", "Onion", "Olive Oil"),
            recipe("9", "Pad Thai", "Pasta", "Thai",
                    "Rice Noodles", "Eggs", "Peanuts", "Lime"),
            recipe("10", "Pancakes", "Dessert", "American",
                    "Plain Flour", "Milk", "Eggs", "Butter"),
            recipe("11", "Almond Cake", "Dessert", "Italian",
                    "Almonds", "Sugar", "Eggs"),
            recipe("12", "Mystery Soup", "Starter", "Unknown"));

    @Override
    public List<Recipe> findCandidates(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return new ArrayList<>(recipes.subList(0, Math.min(limit, recipes.size())));
    }

    private static Recipe recipe(String id, String name, String category, String cuisine,
                                 String... ingredientNames) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredientName : ingredientNames) {
            ingredients.add(new Ingredient(ingredientName));
        }
        return new Recipe(new RecipeKey(PROVIDER, id), name, "", ingredients, "", category, cuisine);
    }
}