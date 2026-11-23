package com.recipefinder.usecases.mealplan.common;

import java.util.Objects;

/** Failure information produced by a scheduling use case. */
public record MealPlanFailure(Code code, String message) {

    public enum Code {
        INVALID_INPUT,
        NOT_SIGNED_IN,
        RECIPE_NOT_SAVED,
        REPLACEMENT_REQUIRED,
        CONFLICT,
        STORAGE_ERROR
    }

    public MealPlanFailure {
        Objects.requireNonNull(code, "code");
        Objects.requireNonNull(message, "message");
    }
}