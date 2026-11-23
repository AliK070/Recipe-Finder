package com.recipefinder.frameworks_drivers.persistence.json.dto;

/** JSON representation of one dinner assignment. */
public record DinnerSlotJson(
        String date,
        String recipeProvider,
        String recipeId,
        Integer numServings) {
}