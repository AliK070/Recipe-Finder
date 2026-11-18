package com.recipefinder.entities;

import java.util.Objects;

public final class Ingredient {
    private final String name;
    private final String measurement;

    public Ingredient(String name, String measurement) {
        this.name = name == null ? "" : name;
        this.measurement = measurement == null ? "" : measurement;
    }

    public String getName() {
        return name;
    }

    public String getMeasurement() {
        return measurement;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Ingredient)) return false;
        Ingredient ingredient = (Ingredient) other;
        return name.equals(ingredient.name) && measurement.equals(ingredient.measurement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, measurement);
    }
}
