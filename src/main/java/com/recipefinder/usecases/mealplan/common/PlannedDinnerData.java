package com.recipefinder.usecases.mealplan.common;

import java.time.LocalDate;
import java.util.Objects;

/** Output data describing one planned dinner. */
public record PlannedDinnerData(
        LocalDate date,
        String recipeKey,
        int numServings) {

    public PlannedDinnerData {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(recipeKey, "recipeKey");
    }
}