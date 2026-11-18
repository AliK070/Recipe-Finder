#!/usr/bin/env bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$ROOT_DIR/build/cli"

mkdir -p "$BUILD_DIR"
find "$ROOT_DIR/src/main/java" -name '*.java' -print0 | \
  xargs -0 javac -d "$BUILD_DIR"

java -cp "$BUILD_DIR" com.recipefinder.ui.RecipeFinderApp "$@"
