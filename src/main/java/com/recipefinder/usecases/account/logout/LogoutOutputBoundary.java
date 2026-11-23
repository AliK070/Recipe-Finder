package com.recipefinder.usecases.account.logout;

/** Receives the result of the logout use case. */
public interface LogoutOutputBoundary {
    void present(LogoutOutputData outputData);
}
