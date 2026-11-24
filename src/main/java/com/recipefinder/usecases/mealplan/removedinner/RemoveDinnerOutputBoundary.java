package com.recipefinder.usecases.mealplan.removedinner;

import com.recipefinder.usecases.mealplan.common.MealPlanFailure;

/** Receives the result of clearing a dinner slot. */
public interface RemoveDinnerOutputBoundary {
    void prepareSuccessView(RemoveDinnerOutputData outputData);
    void prepareFailView(MealPlanFailure failure);
}