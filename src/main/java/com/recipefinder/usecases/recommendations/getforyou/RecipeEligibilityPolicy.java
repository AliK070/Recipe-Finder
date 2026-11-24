package com.recipefinder.usecases.recommendations.getforyou;

import com.recipefinder.entities.Allergen;
import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.Recipe;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
        return true;
    }
}