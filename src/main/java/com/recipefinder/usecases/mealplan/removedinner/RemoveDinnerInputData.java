package com.recipefinder.usecases.mealplan.removedinner;

import java.time.LocalDate;

/** Request to clear one date in a weekly plan. */
public record RemoveDinnerInputData(
        LocalDate weekStart,
        LocalDate date,
        long expectedRevision) {
}