package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validator.annotations.SearchFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchFiltersValidator implements ConstraintValidator<SearchFilters, Map<String, String>> {

    @Override
    public boolean isValid(Map<String, String> searchFilters, ConstraintValidatorContext constraintValidatorContext) {
        if (searchFilters.size() != 2
                || !searchFilters.containsKey("query")
                || !searchFilters.containsKey("by")) {
            return false;
        }

        ArrayList<String> by = new ArrayList<>(List.of(searchFilters.get("by").split(",")));
        ArrayList<String> correctFilters = new ArrayList<>(List.of("title", "director"));
        if (by.size() > 2 || by.isEmpty()) {
            return false;
        }
        for (String filter : by) {
            if (!correctFilters.contains(filter)) {
                return false;
            }
        }

        return true;
    }
}
