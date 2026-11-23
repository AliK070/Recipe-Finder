package com.recipefinder.usecases.mealplan.loadmealplan;

import com.recipefinder.entities.UserId;
import com.recipefinder.entities.WeekKey;
import com.recipefinder.interfaces.MealPlanRepository;
import com.recipefinder.interfaces.MealPlanSession;
import com.recipefinder.interfaces.MealPlanSnapshot;
import com.recipefinder.interfaces.MealPlanStorageException;
import com.recipefinder.usecases.mealplan.common.MealPlanData;
import com.recipefinder.usecases.mealplan.common.MealPlanFailure;
import com.recipefinder.usecases.mealplan.common.MealPlanFailure.Code;
import java.util.Objects;

/** Loads the signed-in user's selected week. */
public final class LoadMealPlanInteractor
        implements LoadMealPlanInputBoundary {

    private final MealPlanRepository repository;
    private final MealPlanSession session;
    private final LoadMealPlanOutputBoundary presenter;

    public LoadMealPlanInteractor(
            MealPlanRepository repository,
            MealPlanSession session,
            LoadMealPlanOutputBoundary presenter) {

        this.repository = Objects.requireNonNull(
                repository, "repository");

        this.session = Objects.requireNonNull(
                session, "session");

        this.presenter = Objects.requireNonNull(
                presenter, "presenter");
    }

    @Override
    public void execute(LoadMealPlanInputData inputData) {

        // Check that a week was supplied.
        if (inputData == null || inputData.weekStart() == null) {
            fail(Code.INVALID_INPUT, "Select a week.");
            return;
        }

        // Obtain the current user from the session boundary.
        UserId userId = session.currentUserId().orElse(null);

        if (userId == null) {
            fail(
                    Code.NOT_SIGNED_IN,
                    "Sign in to view your weekly plan.");
            return;
        }

        // WeekKey enforces the Monday-starting-week rule.
        WeekKey key;

        try {
            key = new WeekKey(userId, inputData.weekStart());
        } catch (IllegalArgumentException exception) {
            fail(Code.INVALID_INPUT, exception.getMessage());
            return;
        }

        // The repository distinguishes a missing week from a read failure.
        MealPlanSnapshot snapshot;
        try {
            snapshot = repository.load(key);
        } catch (MealPlanStorageException exception) {
            fail(Code.STORAGE_ERROR, "The week could not be loaded. Please try again.");
            return;
        }

        // Convert the loaded plan into output data.
        MealPlanData planData = MealPlanData.from(snapshot);

        presenter.prepareSuccessView(
                new LoadMealPlanOutputData(planData));
    }

    private void fail(Code code, String message) {
        presenter.prepareFailView(
                new MealPlanFailure(code, message));
    }
}