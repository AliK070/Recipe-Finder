package com.recipefinder.usecases.account.createaccount;

/** Input required to create a local account. */
public record CreateAccountInputData(String username, String password) {
    @Override
    public String toString() {
        return "CreateAccountInputData[username=" + username + ", password=<redacted>]";
    }
}
