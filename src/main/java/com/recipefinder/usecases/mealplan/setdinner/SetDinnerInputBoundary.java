package com.recipefinder.usecases.mealplan.setdinner;

/** Operation exposed to the assign-dinner controller. */
public interface SetDinnerInputBoundary {
    void execute(SetDinnerInputData inputData);
}