package com.recipefinder.usecases.search.searchrecipes;

public final class SearchRecipesInputData {
    private final String query;

    public SearchRecipesInputData(String query) { this.query = query; }

    public String getQuery() { return query; }
}
