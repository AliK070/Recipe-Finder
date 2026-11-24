package com.recipefinder.usecases.recommendations.getforyou;

import com.recipefinder.entities.UserId;

/** Input identifying whose For You recommendations should be built. */
public record GetForYouInputData(UserId userId) {
}