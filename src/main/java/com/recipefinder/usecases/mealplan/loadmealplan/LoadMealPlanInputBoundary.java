package com.recipefinder.usecases.mealplan.loadmealplan;

/** Operation exposed to the load-week controller. */
public interface LoadMealPlanInputBoundary {
    void execute(LoadMealPlanInputData inputData);
}