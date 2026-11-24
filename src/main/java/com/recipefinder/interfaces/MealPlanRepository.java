package com.recipefinder.interfaces;

import com.recipefinder.entities.MealPlan;
import com.recipefinder.entities.WeekKey;

/** Operations required by the weekly scheduling use case */
public interface MealPlanRepository {
    /**
     * Loads the requested week.
     * A missing week returns an empty plan at revision zero.
     * A failed read throws MealPlanStorageException.
     */
    MealPlanSnapshot load(WeekKey key) throws MealPlanStorageException;

    /**
     * Saves the plan if the stored revision matches expected revision
     * The revision check and write happens together.
     * A successful save returns the plan with its incremented revision.
     * A failed save must preserve the previously stored plan.
     */
    MealPlanSnapshot save(MealPlan plan, long expectedRevision)
            throws MealPlanConflictException, MealPlanStorageException;

}