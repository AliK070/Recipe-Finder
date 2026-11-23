package com.recipefinder.usecases.account.login;

import com.recipefinder.entities.UserId;

/** Result of a login attempt. */
public record LoginOutputData(UserId userId, boolean success, String message) {
    public static LoginOutputData success(UserId userId) {
        return new LoginOutputData(userId, true, "Login successful.");
    }

    public static LoginOutputData failure(String message) {
        return new LoginOutputData(null, false, message);
    }
}
