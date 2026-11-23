package com.recipefinder.entities;

import java.util.Objects;

/** Identifies a local Recipe Finder account. */
public record UserId(String value) {
    public UserId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be blank");
        }
    }
}
