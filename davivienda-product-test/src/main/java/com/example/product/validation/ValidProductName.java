package com.example.product.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validación personalizada para el nombre de producto.
 * No permite caracteres especiales peligrosos.
 */
@Documented
@Constraint(validatedBy = ProductNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductName {

    String message() default "El nombre contiene caracteres no permitidos";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
