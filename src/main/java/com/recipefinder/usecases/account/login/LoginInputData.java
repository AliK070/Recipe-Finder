package com.recipefinder.usecases.account.login;

/** Credentials submitted for login. */
public record LoginInputData(String username, String password) {
    @Override
    public String toString() {
        return "LoginInputData[username=" + username + ", password=<redacted>]";
    }
}
