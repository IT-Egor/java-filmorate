package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validator.annotations.PopularFilmsFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PopularFilmsFiltersValidator implements ConstraintValidator<PopularFilmsFilters, Map<String, String>> {
    @Override
    public boolean isValid(Map<String, String> popularFilmsFilters, ConstraintValidatorContext constraintValidatorContext) {
        ArrayList<String> correctFilters = new ArrayList<>(List.of("genreId", "year", "count"));
        for (String filter : popularFilmsFilters.keySet()) {
            if (!correctFilters.contains(filter)) {
                return false;
            }
        }
        return popularFilmsFilters.size() <= 3;
    }
}
