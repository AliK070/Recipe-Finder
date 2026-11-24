package com.recipefinder.usecases.recommendations.getforyou;

import com.recipefinder.entities.FoodPreferences;
import com.recipefinder.entities.Recipe;
import com.recipefinder.interfaces.PreferencesRepository;
import com.recipefinder.interfaces.RecipeCandidateGateway;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Builds the For You page: loads the user's preferences and candidate recipes,
 * then filters and ranks them.
 */
public final class GetForYouInteractor implements GetForYouInputBoundary {

    static final int CANDIDATE_LIMIT = 24;
    static final String SIGN_IN_MESSAGE = "Sign in to see recommendations.";
    static final String PREFERENCES_ERROR = "Couldn't load your preferences. Please try again.";
    static final String CATALOG_ERROR = "Recipes are unavailable right now. Please try again.";
    static final String NO_MATCHES_MESSAGE =
            "No recipes match your current settings. Try relaxing your preferences.";

    private final PreferencesRepository preferencesRepository;
    private final RecipeCandidateGateway candidateGateway;
    private final RecipeRanker ranker;
    private final GetForYouOutputBoundary presenter;

    public GetForYouInteractor(PreferencesRepository preferencesRepository,
                               RecipeCandidateGateway candidateGateway,
                               RecipeRanker ranker,
                               GetForYouOutputBoundary presenter) {
        this.preferencesRepository = Objects.requireNonNull(preferencesRepository, "preferencesRepository");
        this.candidateGateway = Objects.requireNonNull(candidateGateway, "candidateGateway");
        this.ranker = Objects.requireNonNull(ranker, "ranker");
        this.presenter = Objects.requireNonNull(presenter, "presenter");
    }

    @Override
    public void execute(GetForYouInputData inputData) {
        if (inputData == null || inputData.userId() == null) {
            presenter.prepareFailView(SIGN_IN_MESSAGE);
            return;
        }

        FoodPreferences preferences;
        try {
            preferences = preferencesRepository.findByUserId(inputData.userId())
                    .orElse(FoodPreferences.defaults());
        } catch (RuntimeException exception) {
            presenter.prepareFailView(PREFERENCES_ERROR);
            return;
        }

        List<Recipe> candidates;
        try {
            candidates = candidateGateway.findCandidates(CANDIDATE_LIMIT);
        } catch (IOException | RuntimeException exception) {
            presenter.prepareFailView(CATALOG_ERROR);
            return;
        }

        List<RecommendedRecipe> ranked = ranker.rank(candidates, preferences);
        String message = ranked.isEmpty() ? NO_MATCHES_MESSAGE : "";
        presenter.prepareSuccessView(new GetForYouOutputData(ranked, message));
    }
}