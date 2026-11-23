package com.recipefinder.usecases.account.login;

/** Receives the result of the login use case. */
public interface LoginOutputBoundary {
    void present(LoginOutputData outputData);
}
