package com.recipefinder.usecases.mealplan.removedinner;

/** Operation exposed to the remove-dinner controller. */
public interface RemoveDinnerInputBoundary {
    void execute(RemoveDinnerInputData inputData);
}