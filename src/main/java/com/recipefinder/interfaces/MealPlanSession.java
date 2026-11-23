package com.recipefinder.interfaces;

import com.recipefinder.entities.UserId;
import java.util.Optional;

/** Session information required by weekly scheduling. */
@FunctionalInterface
public interface MealPlanSession {
    Optional<UserId> currentUserId();
}