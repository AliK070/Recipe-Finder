package com.recipefinder.usecases.mealplan.loadmealplan;

import java.time.LocalDate;

/** Request to load the week beginning on the supplied Monday. */
public record LoadMealPlanInputData(LocalDate weekStart) {
}