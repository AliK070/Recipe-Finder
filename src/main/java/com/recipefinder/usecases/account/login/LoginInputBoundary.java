package com.recipefinder.usecases.account.login;

/** Entry point for the login use case. */
public interface LoginInputBoundary {
    void execute(LoginInputData inputData);
}
