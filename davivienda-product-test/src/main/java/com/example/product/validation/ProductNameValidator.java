package com.example.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementación de la regla de validación para el nombre de producto.
 */
public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {

    private static final String PATTERN = "^[A-Za-z0-9 áéíóúÁÉÍÓÚñÑ_-]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(PATTERN);
    }
}
