package com.recipefinder.usecases.mealplan.removedinner;

import com.recipefinder.usecases.mealplan.common.MealPlanData;
import java.time.LocalDate;
import java.util.Objects;

/** Successful result of clearing a dinner slot. */
public record RemoveDinnerOutputData(
        LocalDate date,
        boolean removed,
        MealPlanData plan) {

    public RemoveDinnerOutputData {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(plan, "plan");
    }
}