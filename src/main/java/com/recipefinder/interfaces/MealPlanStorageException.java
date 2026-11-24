package com.recipefinder.interfaces;

/** Indicates that loading or saving a meal plan failed. */
public class MealPlanStorageException extends Exception {

    private static final long serialVersionUID = 1L;

    public MealPlanStorageException(String message) {
        super(message);
    }

    public MealPlanStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}