package com.recipefinder.entities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/** Identifies the user's Monday-to-Sunday meal planning week. */
public record WeekKey(UserId userId, LocalDate weekStart){

    public WeekKey {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(weekStart, "weekStart");

        if (weekStart.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new IllegalArgumentException("The week must start on Monday.");
        }
    }

    /** Returns whether the date belongs to this week. */
    public boolean contains(LocalDate date) {
        Objects.requireNonNull(date, "date");
        long daysFromStart = ChronoUnit.DAYS.between(weekStart, date);
        return daysFromStart >= 0 && daysFromStart < 7;
    }
}
