package com.recipefinder.usecases.mealplan.loadmealplan;

import com.recipefinder.usecases.mealplan.common.MealPlanFailure;

/** Receives the result of the load-week use case. */
public interface LoadMealPlanOutputBoundary {
    void prepareSuccessView(LoadMealPlanOutputData outputData);
    void prepareFailView(MealPlanFailure failure);
}