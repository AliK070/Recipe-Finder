package com.recipefinder.frameworks_drivers.session;

import com.recipefinder.entities.UserId;
import com.recipefinder.interfaces.SessionManager;
import java.util.Objects;
import java.util.Optional;

/** Keeps the active account in memory for the lifetime of the application. */
public final class InMemorySessionManager implements SessionManager {
    private UserId currentUserId;

    @Override
    public synchronized void startSession(UserId userId) {
        currentUserId = Objects.requireNonNull(userId, "userId");
    }

    @Override
    public synchronized Optional<UserId> getCurrentUserId() {
        return Optional.ofNullable(currentUserId);
    }

    @Override
    public synchronized void endSession() {
        currentUserId = null;
    }
}
