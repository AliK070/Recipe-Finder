package com.recipefinder.usecases.recommendations.getforyou;

/** Entry point for building the For You recommendations. */
public interface GetForYouInputBoundary {

    /**
     * Loads the user's preferences and candidate recipes, then filters and ranks them.
     *
     * @param inputData whose recommendations to build
     */
    void execute(GetForYouInputData inputData);
}