package com.recipefinder.usecases.recommendations.getforyou;

/** Receives the result of building the For You recommendations. */
public interface GetForYouOutputBoundary {

    /**
     * Called when recommendations were built (the list may be empty).
     *
     * @param outputData the ranked recipes and any message for the user
     */
    void prepareSuccessView(GetForYouOutputData outputData);

    /**
     * Called when preferences or recipes could not be loaded.
     *
     * @param errorMessage what to show the user
     */
    void prepareFailView(String errorMessage);
}