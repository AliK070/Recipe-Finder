package com.recipefinder.usecases.account.createaccount;

/** Entry point for the create-account use case. */
public interface CreateAccountInputBoundary {
    void execute(CreateAccountInputData inputData);
}
