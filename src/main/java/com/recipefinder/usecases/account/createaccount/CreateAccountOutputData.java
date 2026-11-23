package com.recipefinder.usecases.account.createaccount;

import com.recipefinder.entities.UserId;

/** Result of attempting to create a local account. */
public record CreateAccountOutputData(UserId userId, boolean success, String message) {
    public static CreateAccountOutputData success(UserId userId) {
        return new CreateAccountOutputData(userId, true, "Account created successfully.");
    }

    public static CreateAccountOutputData failure(String message) {
        return new CreateAccountOutputData(null, false, message);
    }
}
