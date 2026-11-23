package com.recipefinder.usecases.mealplan.setdinner;

import com.recipefinder.usecases.mealplan.common.MealPlanData;
import java.time.LocalDate;
import java.util.Objects;

/** Successful result of assigning or replacing a dinner. */
public record SetDinnerOutputData(
        LocalDate date,
        boolean replaced,
        MealPlanData plan) {

    public SetDinnerOutputData {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(plan, "plan");
    }
}