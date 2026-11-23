package com.recipefinder.usecases.mealplan.loadmealplan;

import com.recipefinder.usecases.mealplan.common.MealPlanData;
import java.util.Objects;

/** Successful result of loading a weekly plan. */
public record LoadMealPlanOutputData(MealPlanData plan) {
    public LoadMealPlanOutputData {
        Objects.requireNonNull(plan, "plan");
    }
}