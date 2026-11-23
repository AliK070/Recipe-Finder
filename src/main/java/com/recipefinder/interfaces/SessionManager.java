package com.recipefinder.interfaces;

import com.recipefinder.entities.UserId;
import java.util.Optional;

/** Provides access to the currently signed-in account. */
public interface SessionManager {
    void startSession(UserId userId);

    Optional<UserId> getCurrentUserId();

    void endSession();
}
