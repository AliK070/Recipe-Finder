
package com.recipefinder.entities;

import java.util.Objects;

/** A named ingredient used by a recipe or excluded in food preferences. */
public final class Ingredient {
    private final String name;
    private final String measurement;

    public Ingredient(String name, String measurement) {
        Objects.requireNonNull(name, "name");

        this.name = name.trim();

        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("Ingredient name cannot be blank");
        }

        this.measurement = measurement == null ? "" : measurement.trim();
    }

    public Ingredient(String name) {
        this(name, "");
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
        return name.equals(ingredient.name)
                && measurement.equals(ingredient.measurement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, measurement);
    }
}
