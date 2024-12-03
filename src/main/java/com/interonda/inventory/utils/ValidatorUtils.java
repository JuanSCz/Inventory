package com.interonda.inventory.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorUtils {

    public static <T> void validateEntity(T entity, Validator validator) {
        if (entity == null) {
            throw new IllegalArgumentException("The entity must not be null");
        }
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(errorMessage);
        }
    }
}