package com.recipefinder.usecases.mealplan.removedinner;

import com.recipefinder.entities.MealPlan;
import com.recipefinder.entities.UserId;
import com.recipefinder.entities.WeekKey;
import com.recipefinder.interfaces.MealPlanConflictException;
import com.recipefinder.interfaces.MealPlanRepository;
import com.recipefinder.interfaces.MealPlanSession;
import com.recipefinder.interfaces.MealPlanSnapshot;
import com.recipefinder.interfaces.MealPlanStorageException;
import com.recipefinder.usecases.mealplan.common.MealPlanData;
import com.recipefinder.usecases.mealplan.common.MealPlanFailure;
import com.recipefinder.usecases.mealplan.common.MealPlanFailure.Code;
import java.util.Objects;

/** Clears one dinner slot while preserving the other assignments. */
public final class RemoveDinnerInteractor
        implements RemoveDinnerInputBoundary {

    private final MealPlanRepository repository;
    private final MealPlanSession session;
    private final RemoveDinnerOutputBoundary presenter;

    public RemoveDinnerInteractor(
            MealPlanRepository repository,
            MealPlanSession session,
            RemoveDinnerOutputBoundary presenter) {

        this.repository = Objects.requireNonNull(
                repository, "repository");

        this.session = Objects.requireNonNull(
                session, "session");

        this.presenter = Objects.requireNonNull(
                presenter, "presenter");
    }

    @Override
    public void execute(RemoveDinnerInputData inputData) {

        // Check the required request values.
        if (inputData == null
                || inputData.weekStart() == null
                || inputData.date() == null
                || inputData.expectedRevision() < 0) {

            fail(Code.INVALID_INPUT, "Provide a week, date, and valid revision.");
            return;
        }

        UserId userId = session.currentUserId().orElse(null);

        if (userId == null) {
            fail(Code.NOT_SIGNED_IN, "Sign in to edit your weekly plan.");
            return;
        }

        WeekKey key;

        try {
            key = new WeekKey(userId, inputData.weekStart());
        } catch (IllegalArgumentException exception) {
            fail(Code.INVALID_INPUT, exception.getMessage());
            return;
        }

        if (!key.contains(inputData.date())) {
            fail(Code.INVALID_INPUT, "Select a date within the displayed week.");
            return;
        }

        RemoveDinnerOutputData result;

        try {
            MealPlanSnapshot current = repository.load(key);

            if (current.revision() != inputData.expectedRevision()) {
                fail(Code.CONFLICT, "This week changed. Reload it before editing.");
                return;
            }

            boolean occupied =
                    current.plan().getDinner(inputData.date()).isPresent();

            MealPlanSnapshot saved = current;

            // An already empty date needs no write.
            if (occupied) {
                MealPlan updated =
                        current.plan().withoutDinner(inputData.date());

                saved = repository.save(
                        updated, current.revision());
            }

            result = new RemoveDinnerOutputData(
                    inputData.date(),
                    occupied,
                    MealPlanData.from(saved));

        } catch (MealPlanConflictException exception) {
            fail(Code.CONFLICT, "This week changed. Reload it before editing.");
            return;

        } catch (MealPlanStorageException exception) {
            fail(Code.STORAGE_ERROR, "The dinner could not be removed. Please try again.");
            return;
        }

        presenter.prepareSuccessView(result);
    }

    private void fail(Code code, String message) {
        presenter.prepareFailView(
                new MealPlanFailure(code, message));
    }
}