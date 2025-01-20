package ru.yandex.practicum.filmorate.validator.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.validator.SearchFiltersValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SearchFiltersValidator.class)
public @interface SearchFilters {
    String message() default "Invalid search filters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
