package com.recipefinder.usecases.mealplan.setdinner;

import com.recipefinder.usecases.mealplan.common.MealPlanFailure;

/** Receives the result of assigning a dinner. */
public interface SetDinnerOutputBoundary {

    void prepareSuccessView(SetDinnerOutputData outputData);
    void prepareFailView(MealPlanFailure failure);
}