package com.recipefinder.usecases.account.logout;

import com.recipefinder.interfaces.SessionManager;
import java.util.Objects;

/** Ends the active user session. */
public final class LogoutInteractor implements LogoutInputBoundary {
    private final SessionManager sessionManager;
    private final LogoutOutputBoundary presenter;

    public LogoutInteractor(SessionManager sessionManager, LogoutOutputBoundary presenter) {
        this.sessionManager = Objects.requireNonNull(sessionManager, "sessionManager");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(LogoutInputData inputData) {
        try {
            sessionManager.endSession();
            presenter.present(LogoutOutputData.loggedOut());
        } catch (RuntimeException exception) {
            presenter.present(LogoutOutputData.failure("Could not end the session. Please try again."));
        }
    }
}
