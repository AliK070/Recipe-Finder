package com.recipefinder.usecases.account.logout;

/** Entry point for the logout use case. */
public interface LogoutInputBoundary {
    void execute(LogoutInputData inputData);
}
