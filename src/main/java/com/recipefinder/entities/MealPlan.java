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

    /**
     * Creates an empty meal plan.
     */
    public MealPlan(WeekKey weekKey) {
        this(weekKey, Map.of());
    }

    /**
     * Creates a plan from existing assignmens, such as data loaded from storage.
     */
    public MealPlan(WeekKey weekKey, Map<LocalDate, DinnerSlot> dinners) {
        this.weekKey = Objects.requireNonNull(weekKey, "weekKey");
        Objects.requireNonNull(dinners, "dinners");

        for (Map.Entry<LocalDate, DinnerSlot> entry : dinners.entrySet()) {
            this.weekKey = Objects.requireNonNull(weekKey, "weekKey");
            Objects.requireNonNull(dinners, "dinners");

            for (Map.Entry<LocalDate, DinnerSlot> entry : dinners.entrySet()) {
                requireDateInWeek(entry.getKey());
                Objects.requireNonNull(entry.getValue(), "dinner");
            }

            // Prevent callers from changing the plan through the supplied map.
            this.dinners = Map.copyOf(dinners);
        }

        public WeekKey getWeekKey() {
            return weekKey;
        }

        /** Returns an unmodifiable map; an absent date represents an empty slot. */
        public Map<LocalDate, DinnerSlot> getDinners() {
            return dinners;
        }

        /** Returns the assigned dinner, or Optional.empty() for an empty slot. */
        public Optional<DinnerSlot> getDinner(LocalDate date) {
            requireDateInWeek(date);
            return Optional.ofNullable(dinners.get(date));
        }

        /** Returns a new plan with a dinner added or replaced on the given date. */
        public MealPlan withDinner(LocalDate date, DinnerSlot dinner) {
            requireDateInWeek(date);
            Objects.requireNonNull(dinner, "dinner");

            Map<LocalDate, DinnerSlot> updatedDinners = new HashMap<>(dinners);
            updatedDinners.put(date, dinner);
            return new MealPlan(weekKey, updatedDinners);
        }

        /** Returns a new plan with the date cleared, even if it was already empty. */
        public MealPlan withoutDinner(LocalDate date) {
            requireDateInWeek(date);

            Map<LocalDate, DinnerSlot> updatedDinners = new HashMap<>(dinners);
            updatedDinners.remove(date);
            return new MealPlan(weekKey, updatedDinners);
        }

        private void requireDateInWeek(LocalDate date) {
            Objects.requireNonNull(date, "date");

            if (!weekKey.contains(date)) {
                throw new IllegalArgumentException("The date must belong to this plan's week.");
            }
        }
    }
}