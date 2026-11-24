package com.recipefinder.interfaces;

/** Indicates that the stored week changed after it was loaded. */
public class MealPlanConflictException extends Exception {

    private static final long serialVersionUID = 1L;

    public MealPlanConflictException(String message) {
        super(message);
    }
}