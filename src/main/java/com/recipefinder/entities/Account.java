package com.recipefinder.entities;

import java.util.Objects;

/** A local user account and its saved food preferences. */
public record Account(UserId userId, String username, String password, FoodPreferences foodPreferences) {
    public Account {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(password, "password");
        Objects.requireNonNull(foodPreferences, "foodPreferences");

        username = username.trim();
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
    }
}
