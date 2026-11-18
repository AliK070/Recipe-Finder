package com.recipefinder.frameworks_drivers.api.themealdb;

import com.recipefinder.frameworks_drivers.api.themealdb.dto.TheMealDBRecipeDto;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TheMealDBClient {
    private static final int MAX_INGREDIENTS = 20;
    private final String baseUrl;
    private final HttpClient httpClient;

    public TheMealDBClient() {
        this("https://www.themealdb.com/api/json/v1/1");
    }

    public TheMealDBClient(String baseUrl) {
        this(baseUrl, HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build());
    }

    public TheMealDBClient(String baseUrl, HttpClient httpClient) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("baseUrl must not be blank");
        }
        if (httpClient == null) {
            throw new IllegalArgumentException("httpClient must not be null");
        }
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.httpClient = httpClient;
    }

    public List<TheMealDBRecipeDto> search(String query) throws IOException {
        if (query == null || query.trim().isEmpty()) return new ArrayList<>();
        String encoded = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);
        return parseMeals(get(baseUrl + "/search.php?s=" + encoded));
    }

    public List<TheMealDBRecipeDto> lookup(String id) throws IOException {
        if (id == null || id.trim().isEmpty()) throw new IOException("Recipe id is missing");
        String encoded = URLEncoder.encode(id.trim(), StandardCharsets.UTF_8);
        return parseMeals(get(baseUrl + "/lookup.php?i=" + encoded));
    }

    private String get(String url) throws IOException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("TheMealDB returned HTTP " + response.statusCode());
            }
            return response.body();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("TheMealDB request was interrupted", exception);
        }
    }

    public List<TheMealDBRecipeDto> parseMeals(String json) {
        List<TheMealDBRecipeDto> meals = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) {
            return meals;
        }

        int depth = 0;
        int objectStart = -1;
        boolean quoted = false;
        boolean escaped = false;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (quoted) {
                if (escaped) {
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    quoted = false;
                }
                continue;
            }
            if (c == '"') {
                quoted = true;
                continue;
            }
            if (c == '{') {
                if (depth == 0) objectStart = i;
                depth++;
            } else if (c == '}' && depth > 0) {
                depth--;
                if (depth == 0 && objectStart >= 0) {
                    meals.add(parseMeal(json.substring(objectStart, i + 1)));
                    objectStart = -1;
                }
            }
        }
        return meals;
    }

    private TheMealDBRecipeDto parseMeal(String object) {
        TheMealDBRecipeDto dto = new TheMealDBRecipeDto();
        dto.setIdMeal(field(object, "idMeal"));
        dto.setStrMeal(field(object, "strMeal"));
        dto.setStrInstructions(field(object, "strInstructions"));
        dto.setStrMealThumb(field(object, "strMealThumb"));
        dto.setStrCategory(field(object, "strCategory"));
        dto.setStrArea(field(object, "strArea"));
        for (int i = 1; i <= MAX_INGREDIENTS; i++) {
            dto.setIngredient(i, field(object, "strIngredient" + i));
            dto.setMeasure(i, field(object, "strMeasure" + i));
        }
        return dto;
    }

    private String field(String object, String name) {
        String marker = "\"" + name + "\"";
        int nameIndex = object.indexOf(marker);
        if (nameIndex < 0) {
            return null;
        }
        int colon = object.indexOf(':', nameIndex + marker.length());
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < object.length() && Character.isWhitespace(object.charAt(start))) start++;
        if (start >= object.length()) return null;
        if (object.charAt(start) != '"') return null;

        StringBuilder value = new StringBuilder();
        boolean escaped = false;
        for (int i = start + 1; i < object.length(); i++) {
            char c = object.charAt(i);
            if (escaped) {
                appendEscapedCharacter(value, c, object, i);
                if (c == 'u' && i + 4 < object.length()) {
                    i += 4;
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                return value.toString();
            } else {
                value.append(c);
            }
        }
        return null;
    }

    private void appendEscapedCharacter(StringBuilder value, char escapedCharacter,
                                        String object, int index) {
        switch (escapedCharacter) {
            case 'n': value.append('\n'); break;
            case 'r': value.append('\r'); break;
            case 't': value.append('\t'); break;
            case '"': value.append('"'); break;
            case '\\': value.append('\\'); break;
            case '/': value.append('/'); break;
            case 'u':
                if (index + 4 < object.length()) {
                    try {
                        value.append((char) Integer.parseInt(
                                object.substring(index + 1, index + 5), 16));
                    } catch (NumberFormatException exception) {
                        value.append('u');
                    }
                } else {
                    value.append('u');
                }
                break;
            default: value.append(escapedCharacter); break;
        }
    }
}
