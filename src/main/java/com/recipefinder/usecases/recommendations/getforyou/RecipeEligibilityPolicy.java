package com.recipefinder.usecases.recommendations.getforyou;

import com.recipefinder.entities.Allergen;
import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.Recipe;
import com.recipefinder.entities.DietaryType;
import com.recipefinder.entities.Ingredient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Locale;

/**
 * Decides whether a recipe is safe to recommend for a user's food preferences.
 * Rules are conservative: when in doubt, the recipe is excluded.
 */
public final class RecipeEligibilityPolicy {

    /** Ingredient words that indicate each allergen. */
    private static final Map<Allergen, Set<String>> ALLERGEN_TERMS = Map.of(
            Allergen.MILK, Set.of("milk", "butter", "cheese", "cream", "yogurt", "yoghurt",
                    "ghee", "parmesan", "mozzarella", "cheddar", "feta", "ricotta",
                    "mascarpone", "buttermilk", "custard", "creme fraiche"),
            Allergen.EGGS, Set.of("egg", "eggs", "mayonnaise"),
            Allergen.FISH, Set.of("fish", "salmon", "tuna", "cod", "haddock", "anchovy",
                    "anchovies", "sardines", "mackerel", "trout", "herring", "tilapia"),
            Allergen.SHELLFISH, Set.of("prawn", "prawns", "shrimp", "shrimps", "crab",
                    "lobster", "mussels", "clams", "oysters", "scallops", "squid"),
            Allergen.PEANUTS, Set.of("peanut", "peanuts"),
            Allergen.TREE_NUTS, Set.of("almond", "almonds", "walnut", "walnuts", "cashew",
                    "cashews", "pecan", "pecans", "hazelnut", "hazelnuts", "pistachio",
                    "pistachios", "macadamia", "pine nuts"),
            Allergen.WHEAT, Set.of("flour", "bread", "breadcrumbs", "pasta", "spaghetti",
                    "penne", "lasagne", "noodles", "couscous", "wheat", "tortillas",
                    "pastry", "semolina", "bulgur", "barley", "rye", "soy sauce"),
            Allergen.SOY, Set.of("soy", "soya", "tofu", "edamame", "miso", "tempeh"),
            Allergen.SESAME, Set.of("sesame", "tahini"),
            Allergen.MUSTARD, Set.of("mustard"));

    /** Phrases that contain a milk word but are not dairy. */
    private static final List<String> NOT_DAIRY = List.of(
            "peanut butter", "butter beans", "coconut milk", "coconut cream",
            "almond milk", "oat milk", "soy milk", "cream of tartar", "cocoa butter");

    /** Ingredient words that indicate meat. */
    private static final Set<String> MEAT_TERMS = Set.of(
            "beef", "chicken", "pork", "lamb", "bacon", "ham", "sausage", "sausages",
            "mince", "turkey", "duck", "goat", "veal", "chorizo", "salami", "pancetta",
            "prosciutto", "steak", "venison", "gelatine", "gelatin", "lard");

    /** TheMealDB categories that are meat dishes. */
    private static final Set<String> MEAT_CATEGORIES = Set.of(
            "beef", "chicken", "pork", "lamb", "goat");

    private static final String SEAFOOD_CATEGORY = "seafood";

    public boolean isEligible(Recipe recipe, FoodPreferences preferences) {
        if (!hasRestrictions(preferences)) {
            return true;
        }
        if (recipe.getIngredients().isEmpty()) {
            return false;
        }
        if (!fitsDiet(recipe, preferences.dietaryType())) {
            return false;
        }
        for (Allergen allergen : preferences.excludedAllergens()) {
            if (containsAllergen(recipe, allergen)) {
                return false;
            }
        }
        for (Ingredient excluded : preferences.excludedIngredients()) {
            if (anyIngredientMatches(recipe, Set.of(normalize(excluded.getName()).trim()))) {
                return false;
            }
        }
        return true;
    }
    private boolean hasRestrictions(FoodPreferences preferences) {
        return preferences.dietaryType() != DietaryType.NONE
                || !preferences.excludedAllergens().isEmpty()
                || !preferences.excludedIngredients().isEmpty();
    }

    private boolean fitsDiet(Recipe recipe, DietaryType diet) {
        String category = recipe.getCategory().trim().toLowerCase(Locale.ROOT);
        boolean meatCategory = MEAT_CATEGORIES.contains(category);
        boolean seafoodCategory = SEAFOOD_CATEGORY.equals(category);
        boolean hasMeat = anyIngredientMatches(recipe, MEAT_TERMS);
        boolean hasSeafood = containsAllergen(recipe, Allergen.FISH)
                || containsAllergen(recipe, Allergen.SHELLFISH);

        return switch (diet) {
            case NONE -> true;
            case PESCATARIAN -> !meatCategory && !hasMeat;
            case VEGETARIAN -> !meatCategory && !seafoodCategory && !hasMeat && !hasSeafood;
            case VEGAN -> fitsDiet(recipe, DietaryType.VEGETARIAN)
                    && !containsAllergen(recipe, Allergen.MILK)
                    && !containsAllergen(recipe, Allergen.EGGS)
                    && !anyIngredientMatches(recipe, Set.of("honey"));
            case GLUTEN_FREE -> !containsAllergen(recipe, Allergen.WHEAT);
            case DAIRY_FREE -> !containsAllergen(recipe, Allergen.MILK);
        };
    }

    private boolean containsAllergen(Recipe recipe, Allergen allergen) {
        Set<String> terms = ALLERGEN_TERMS.get(allergen);
        for (Ingredient ingredient : recipe.getIngredients()) {
            String name = normalize(ingredient.getName());
            if (allergen == Allergen.MILK) {
                for (String safe : NOT_DAIRY) {
                    name = name.replace(" " + safe + " ", " ");
                }
            }
            if (containsAnyTerm(name, terms)) {
                return true;
            }
        }
        return false;
    }

    private boolean anyIngredientMatches(Recipe recipe, Set<String> terms) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (containsAnyTerm(normalize(ingredient.getName()), terms)) {
                return true;
            }
        }
        return false;
    }

    /** Whole-word match: "egg" matches "egg yolks" but not "eggplant". */
    private boolean containsAnyTerm(String normalizedName, Set<String> terms) {
        for (String term : terms) {
            if (normalizedName.contains(" " + term + " ")) {
                return true;
            }
        }
        return false;
    }

    /** Lower-cases, turns punctuation into spaces, and pads with spaces for whole-word matching. */
    private static String normalize(String text) {
        return " " + text.toLowerCase(Locale.ROOT).replaceAll("[^a-z]+", " ").trim() + " ";
    }
}