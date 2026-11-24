package com.recipefinder.usecases.mealplan.common;

import com.recipefinder.entities.MealPlan;
import com.recipefinder.interfaces.MealPlanSnapshot;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Immutable output data describing one user's weekly plan. */
public record MealPlanData(
        String userId,
        LocalDate weekStart,
        long revision,
        List<PlannedDinnerData> dinners) {

    public MealPlanData {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(weekStart, "weekStart");

        dinners = List.copyOf(
                Objects.requireNonNull(dinners, "dinners"));
    }

    public static MealPlanData from(MealPlanSnapshot snapshot) {
        Objects.requireNonNull(snapshot, "snapshot");

        MealPlan plan = snapshot.plan();

        List<PlannedDinnerData> dinners =
                plan.getDinners().entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(entry -> new PlannedDinnerData(
                                entry.getKey(),
                                entry.getValue().recipeKey().getProvider(),
                                entry.getValue().recipeKey().getId(),
                                entry.getValue().numServings()))
                        .toList();

        return new MealPlanData(
                plan.getWeekKey().userId().value(),
                plan.getWeekKey().weekStart(),
                snapshot.revision(),
                dinners);
    }
}