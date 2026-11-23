package com.recipefinder.ui;

import com.recipefinder.entities.Recipe;
import com.recipefinder.entities.RecipeKey;
import com.recipefinder.entities.RecipeSummary;
import com.recipefinder.frameworks_drivers.api.themealdb.TheMealDBRecipeGateway;
import com.recipefinder.usecases.search.getrecipedetails.GetRecipeDetailsInputData;
import com.recipefinder.usecases.search.getrecipedetails.GetRecipeDetailsInteractor;
import com.recipefinder.usecases.search.getrecipedetails.GetRecipeDetailsOutputBoundary;
import com.recipefinder.usecases.search.getrecipedetails.GetRecipeDetailsOutputData;
import com.recipefinder.usecases.search.searchrecipes.SearchRecipesInputData;
import com.recipefinder.usecases.search.searchrecipes.SearchRecipesInteractor;
import com.recipefinder.usecases.search.searchrecipes.SearchRecipesOutputBoundary;
import com.recipefinder.usecases.search.searchrecipes.SearchRecipesOutputData;

import java.util.Scanner;

public final class RecipeFinderApp {
    public static void main(String[] args) {
        TheMealDBRecipeGateway gateway = new TheMealDBRecipeGateway();

        Scanner scanner = new Scanner(System.in);
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("details") && args.length > 1) {
                runDetails(gateway, args[1]);
            } else {
                runSearch(gateway, String.join(" ", args));
            }
            return;
        }

        System.out.println("Recipe Finder - TheMealDB search");
        System.out.println("Type a recipe name, or 'exit' to quit.");
        while (true) {
            System.out.print("Search: ");
            String query = scanner.nextLine().trim();
            if (query.equalsIgnoreCase("exit") || query.equalsIgnoreCase("quit")) return;
            runSearch(gateway, query);
        }
    }

    private static void runSearch(TheMealDBRecipeGateway gateway, String query) {
        SearchRecipesInteractor search = new SearchRecipesInteractor(gateway,
                new SearchRecipesOutputBoundary() {
                    @Override
                    public void prepareSuccessView(SearchRecipesOutputData output) {
                        if (output.getRecipes().isEmpty()) {
                            System.out.println("No recipes found.");
                            return;
                        }
                        System.out.println("Search results:");
                        for (RecipeSummary recipe : output.getRecipes()) {
                            System.out.printf("%s | %s | %s | %s%n",
                                    recipe.getKey().getId(), recipe.getName(),
                                    recipe.getCategory(), recipe.getCuisine());
                        }
                        System.out.println("Use: RecipeFinderApp details <ID>");
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        System.out.println(errorMessage);
                    }
                });

        search.execute(new SearchRecipesInputData(query));
    }

    private static void runDetails(TheMealDBRecipeGateway gateway, String id) {
        GetRecipeDetailsInteractor details = new GetRecipeDetailsInteractor(gateway,
                new GetRecipeDetailsOutputBoundary() {
                    @Override
                    public void prepareSuccessView(GetRecipeDetailsOutputData output) {
                        Recipe recipe = output.getRecipe();
                        if (recipe == null) {
                            System.out.println("Recipe not found.");
                            return;
                        }
                        System.out.println("Name: " + recipe.getName());
                        System.out.println("Category: " + recipe.getCategory());
                        System.out.println("Cuisine: " + recipe.getCuisine());
                        System.out.println("Ingredients:");
                        recipe.getIngredients().forEach(ingredient ->
                                System.out.println("- " + ingredient.getName() + " (" + ingredient.getMeasurement() + ")"));
                        System.out.println("Instructions:\n" + recipe.getInstructions());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        System.out.println(errorMessage);
                    }
                });
        details.execute(new GetRecipeDetailsInputData(new RecipeKey("themealdb", id)));
    }
}
