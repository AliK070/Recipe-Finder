package com.recipefinder.usecases.mealplan.setdinner;

import com.recipefinder.entities.DinnerSlot;
import com.recipefinder.entities.MealPlan;
import com.recipefinder.entities.RecipeKey;
import com.recipefinder.entities.UserId;
import com.recipefinder.entities.WeekKey;
import com.recipefinder.interfaces.MealPlanConflictException;
import com.recipefinder.interfaces.MealPlanRepository;
import com.recipefinder.interfaces.MealPlanSession;
import com.recipefinder.interfaces.MealPlanSnapshot;
import com.recipefinder.interfaces.MealPlanStorageException;
import com.recipefinder.interfaces.SavedRecipeLookup;
import com.recipefinder.usecases.mealplan.common.MealPlanData;
import com.recipefinder.usecases.mealplan.common.MealPlanFailure;
import com.recipefinder.usecases.mealplan.common.MealPlanFailure.Code;
import java.util.Objects;

/** Assigns or explicitly replaces a saved recipe in a weekly plan. */
public final class SetDinnerInteractor
        implements SetDinnerInputBoundary {

    private final MealPlanRepository repository;
    private final MealPlanSession session;
    private final SavedRecipeLookup savedRecipes;
    private final SetDinnerOutputBoundary presenter;

    public SetDinnerInteractor(
            MealPlanRepository repository,
            MealPlanSession session,
            SavedRecipeLookup savedRecipes,
            SetDinnerOutputBoundary presenter) {

        this.repository = Objects.requireNonNull(
                repository, "repository");

        this.session = Objects.requireNonNull(
                session, "session");

        this.savedRecipes = Objects.requireNonNull(
                savedRecipes, "savedRecipes");

        this.presenter = Objects.requireNonNull(
                presenter, "presenter");
    }

    @Override
    public void execute(SetDinnerInputData inputData) {

        // Check the required request values.
        if (inputData == null
                || inputData.weekStart() == null
                || inputData.date() == null
                || inputData.recipeKey() == null
                || inputData.expectedRevision() < 0) {

            fail(Code.INVALID_INPUT, "Provide a week, date, recipe, and valid revision.");
            return;
        }

        // Determine whose weekly plan is being edited.
        UserId userId = session.currentUserId().orElse(null);

        if (userId == null) {
            fail(Code.NOT_SIGNED_IN, "Sign in to plan a dinner.");
            return;
        }

        // Let the domain types enforce their own rules.
        WeekKey key;
        DinnerSlot dinner;

        try {
            key = new WeekKey(userId, inputData.weekStart());

            RecipeKey recipeKey =
                    new RecipeKey(inputData.recipeKey().trim());

            dinner = new DinnerSlot(
                    recipeKey, inputData.numServings());

        } catch (IllegalArgumentException exception) {
            fail(Code.INVALID_INPUT, exception.getMessage());
            return;
        }

        if (!key.contains(inputData.date())) {
            fail(Code.INVALID_INPUT, "Select a date within the displayed week.");
            return;
        }

        SetDinnerOutputData result;

        try {
            // Start with the existing plan, preserving its other dates.
            MealPlanSnapshot current = repository.load(key);

            if (current.revision() != inputData.expectedRevision()) {
                fail(Code.CONFLICT, "This week changed. Reload it before editing.");
                return;
            }

            if (!savedRecipes.isSaved(userId, dinner.recipeKey())) {
                fail(Code.RECIPE_NOT_SAVED, "Choose a recipe from your saved recipes.");
                return;
            }

            boolean occupied =
                    current.plan().getDinner(inputData.date()).isPresent();

            if (occupied && !inputData.replacementConfirmed()) {
                fail(Code.REPLACEMENT_REQUIRED, "Confirm replacement of the existing dinner.");
                return;
            }

            // Create a new plan without modifying the original.
            MealPlan updated = current.plan().withDinner(
                    inputData.date(), dinner);

            // The repository must also check the revision during saving.
            MealPlanSnapshot saved = repository.save(
                    updated, current.revision());

            result = new SetDinnerOutputData(
                    inputData.date(),
                    occupied,
                    MealPlanData.from(saved));

        } catch (MealPlanConflictException exception) {
            fail(Code.CONFLICT, "This week changed. Reload it before editing.");
            return;

        } catch (MealPlanStorageException exception) {
            fail(Code.STORAGE_ERROR, "The dinner could not be saved. Please try again.");
            return;
        }

        // Report success only after saving succeeds.
        presenter.prepareSuccessView(result);
    }

    private void fail(Code code, String message) {
        presenter.prepareFailView(new MealPlanFailure(code, message));
    }
}