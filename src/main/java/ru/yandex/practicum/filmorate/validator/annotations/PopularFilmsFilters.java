package ru.yandex.practicum.filmorate.validator.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.validator.PopularFilmsFiltersValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PopularFilmsFiltersValidator.class)
public @interface PopularFilmsFilters {
    String message() default "Invalid popular films filters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
