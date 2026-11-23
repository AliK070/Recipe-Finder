package com.recipefinder.usecases.mealplan.setdinner;

import java.time.LocalDate;

/** Request to assign or replace a dinner. */
public record SetDinnerInputData(
        LocalDate weekStart,
        LocalDate date,
        String recipeKey,
        int numServings,
        long expectedRevision,
        boolean replacementConfirmed) {
}