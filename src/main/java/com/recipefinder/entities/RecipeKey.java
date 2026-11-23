package com.recipefinder.entities;

import java.util.Objects;

public final class RecipeKey {
    private final String provider;
    private final String id;

    public RecipeKey(String provider, String id) {
        if (provider == null || provider.trim().isEmpty()) {
            throw new IllegalArgumentException("provider must not be blank");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        this.provider = provider.trim();
        this.id = id.trim();
    }

    public String getProvider() {
        return provider;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof RecipeKey)) return false;
        RecipeKey recipeKey = (RecipeKey) other;
        return provider.equals(recipeKey.provider) && id.equals(recipeKey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, id);
    }

    @Override
    public String toString() {
        return provider + ":" + id;
    }
}
