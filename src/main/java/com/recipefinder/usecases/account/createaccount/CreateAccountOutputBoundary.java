package com.recipefinder.usecases.account.createaccount;

/** Receives the result of the create-account use case. */
public interface CreateAccountOutputBoundary {
    void present(CreateAccountOutputData outputData);
}
