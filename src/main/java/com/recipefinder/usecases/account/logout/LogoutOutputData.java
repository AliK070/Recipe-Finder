package com.recipefinder.usecases.account.logout;

/** Result of ending the active session. */
public record LogoutOutputData(boolean success, String message) {
    public static LogoutOutputData loggedOut() {
        return new LogoutOutputData(true, "Logout successful.");
    }

    public static LogoutOutputData failure(String message) {
        return new LogoutOutputData(false, message);
    }
}
