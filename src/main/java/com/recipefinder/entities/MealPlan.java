package com.recipefinder.entities;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** An immutable collection of dinner assignments for one user's week. */
public final class MealPlan {

    private final WeekKey weekKey;
    private final Map<LocalDate, DinnerSlot> dinners;

    /** Creates an empty meal plan. */
    public MealPlan(WeekKey weekKey) {
        this(weekKey, Map.of());
    }

    /** Creates a plan from existing assignments. */
    public MealPlan(WeekKey weekKey, Map<LocalDate, DinnerSlot> dinners) {
        this.weekKey = Objects.requireNonNull(weekKey, "weekKey");
        Objects.requireNonNull(dinners, "dinners");

        // Validate each assignment once.
        for (Map.Entry<LocalDate, DinnerSlot> entry : dinners.entrySet()) {
            requireDateInWeek(entry.getKey());
            Objects.requireNonNull(entry.getValue(), "dinner");
        }

        // Copy the map after validation, even when it is empty.
        this.dinners = Map.copyOf(dinners);
    }

    public WeekKey getWeekKey() {
        return weekKey;
    }

    /** Returns an unmodifiable map of the dinner assignments. */
    public Map<LocalDate, DinnerSlot> getDinners() {
        return dinners;
    }

    /** Returns the dinner, or Optional.empty() for an unassigned date. */
    public Optional<DinnerSlot> getDinner(LocalDate date) {
        requireDateInWeek(date);
        return Optional.ofNullable(dinners.get(date));
    }

    /** Returns a new plan with a dinner added or replaced. */
    public MealPlan withDinner(LocalDate date, DinnerSlot dinner) {
        requireDateInWeek(date);
        Objects.requireNonNull(dinner, "dinner");

        Map<LocalDate, DinnerSlot> updatedDinners = new HashMap<>(dinners);
        updatedDinners.put(date, dinner);

        return new MealPlan(weekKey, updatedDinners);
    }

    /** Returns a new plan with the selected date cleared. */
    public MealPlan withoutDinner(LocalDate date) {
        requireDateInWeek(date);

        Map<LocalDate, DinnerSlot> updatedDinners = new HashMap<>(dinners);
        updatedDinners.remove(date);

        return new MealPlan(weekKey, updatedDinners);
    }

    private void requireDateInWeek(LocalDate date) {
        Objects.requireNonNull(date, "date");

        if (!weekKey.contains(date)) {
            throw new IllegalArgumentException(
                    "The date must belong to this plan's week.");
        }
    }
}