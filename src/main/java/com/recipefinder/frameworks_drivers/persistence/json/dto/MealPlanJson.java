package com.recipefinder.frameworks_drivers.persistence.json.dto;

import java.util.List;

/** JSON representation of one user's weekly plan. */
public record MealPlanJson(
        String userId,
        String weekStart,
        Long revision,
        List<DinnerSlotJson> dinners) {
}