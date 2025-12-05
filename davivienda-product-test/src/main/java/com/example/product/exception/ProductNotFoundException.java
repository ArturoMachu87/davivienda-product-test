package com.example.product.exception;

/**
 * author Arturo Machuca
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Producto con id " + id + " no encontrado");
    }
}
