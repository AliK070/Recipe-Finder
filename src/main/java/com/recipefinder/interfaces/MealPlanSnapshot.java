package com.recipefinder.interfaces;

import com.recipefinder.entities.MealPlan;
import java.util.Objects;

/** A meal plan together with its storage revision. */
public record MealPlanSnapshot(MealPlan plan, long revision) {

    public MealPlanSnapshot {
        Objects.requireNonNull(plan, "plan");

        if (revision < 0) {
            throw new IllegalArgumentException("Revision cannot be negative.");
        }

        if (revision == 0 && !plan.getDinners().isEmpty()){
            throw new IllegalArgumentException("An unsaved week must be empty.");
        }
    }
}