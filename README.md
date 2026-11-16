# Recipe-Finder

## Team Information

### Team Members
- Ali Khan
- Onuralp Eranil
- Ozan Sezen
- Nizar Al Issa

---

## Project Description

### Domain

Recipe Finder is a desktop Java Swing application for home cooks who want to discover recipes, keep favorites, and plan dinners for the week. A cook can create a local profile, set food preferences, search TheMealDB, and browse personalized suggestions on the For You Page.

TheMealDB supplies recipe data. The application filters and ranks recommendations locally using saved preferences. Profile settings, favorites, and dinner plans are saved in local JSON.

---

## User Stories 

### User Story 1 · Create a Profile and Preferences

As a cook, I want to create a profile with my food preferences so the app remembers my choices when I return, can also save the ones I like so I can come back to them.

### User Story 2 · Search Recipes

As a cook, I want to search for recipes by name and view their ingredients and instructions so I can decide what to make.

### User Story 3 · Discover Recipes on the For You Page

As a cook, I want to see recipes that match my saved preferences according to my profile.

### User Story 4 · Plan Dinners for the Week

As a cook, I want to assign saved recipes to dates in my weekly schedule so I can reopen my plan and see what to cook.

---

## Use Cases

### Use Case 1 · Create Profile and Preferences

**Lead.** Onuralp Eranil
**Precondition.** The cook is signed out and has opened the profile creation screen.
**Success outcome.** The app saves the new local profile and makes it available for sign-in.

1. The cook enters the required profile information and submits it.
2. The app validates the information and checks for an existing profile with the same username.
3. The app saves the new profile and shows confirmation.
4. The cook can set dietary preferences and ingredient exclusions for the profile.
5. The cook can save their favorite recipes to access later.

**Alternate flows.** Invalid input or a duplicate username prompts a correction. A storage error shows feedback and keeps the form available for retry.

### Use Case 2 · Search Recipes

**Lead.** Ali Khan  
**Precondition.** The cook has opened Search.  
**Success outcome.** The cook sees matching recipes and can open a result for details.

1. The cook enters a recipe name or partial name.
2. The app validates the query and asks TheMealDB for matching recipes.
3. The app shows the results. Available thumbnails load in the background.
4. The cook opens a result to see its ingredients and instructions.

**Alternate flows.** A blank query prompts the cook to enter a name. An empty result shows a no-matches message. An API failure keeps the query visible for retry. If a thumbnail fails, the app shows a placeholder.

### Use Case 3 · Build the For You Page

**Lead.** Nizar Al Issa  
**Precondition.** The cook is signed in and the app has loaded their saved preferences.  
**Success outcome.** The cook sees eligible recipes ranked by preference match and can save a recipe to Favorites.

1. The cook opens or refreshes the For You Page.
2. The app loads a bounded set of TheMealDB recipes.
3. The app filters candidates using saved dietary and ingredient exclusions.
4. The app ranks eligible recipes locally using preferred cuisines or categories and displays the reasons for the matches.

**Alternate flows.** The app distinguishes an unavailable catalog from a successful load with zero eligible recipes. Saving a recipe that is already in Favorites keeps a single entry.

### Use Case 4 · Plan Dinners for the Week

**Lead.** Ozan Sezen  
**Precondition.** The cook is signed in, has opened a week, and has a saved recipe to choose from.  
**Success outcome.** The dinner appears on the selected date and reloads after restart.

1. The cook selects a day in the displayed Monday–Sunday week.
2. The cook chooses a saved recipe and enters a serving count from 1 to 12.
3. The app validates the selection and checks whether the day already has a dinner.
4. The cook confirms the replacement if the day is occupied.
5. The app saves the plan and updates the weekly schedule.

**Alternate flows.** Invalid input prompts a correction. Canceling a replacement preserves the existing dinner. A conflicting edit prompts the cook to reload the week. A storage error leaves the last saved plan visible.

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
| --- | --- |
| Ozan Sezen | **Week** — Load a week, assign or replace a dinner, clear a dinner, and reload the saved plan. |
| Onuralp Eranil | **Create Profile** — Create a local profile and support saving and reloading its preferences. |
| Ali Khan | **Search Recipes** — Search TheMealDB by name and open a matching recipe for details. |
| Nizar Al Issa | **For You Page** — Filter and rank recipes locally, and support saving favorites. |

**Optional follow-up.** Weekly autofill can preview dinners for empty slots using eligible For You results. The cook reviews the preview before confirming the changes.

---

## Entities

### Profile and Food Preferences

`ProfileId` identifies a local profile. `FoodPreferences` stores dietary choices, ingredient or allergy exclusions, and optional cuisine or category preferences.

### Recipe and Recipe Key

`RecipeKey` identifies a recipe by its provider and recipe ID. `RecipeSummary` carries information for lists, such as the title and thumbnail. `Recipe` holds details such as ingredients and instructions.

### Favorite Collection

`FavoriteCollection` associates a profile with a set of saved recipe keys. Search, For You, and Week use the same `RecipeKey` for a saved recipe.

### Weekly Meal Plan

`WeekKey` identifies a plan by profile and the Monday of the selected week. `MealPlan` maps dates to dinner slots, with at most one dinner per date. `DinnerSlot` holds a recipe key and a serving count from 1 to 12.