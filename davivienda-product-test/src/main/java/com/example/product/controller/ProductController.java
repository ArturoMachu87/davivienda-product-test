package com.example.product.controller;

import com.example.product.dto.PagedResponseDto;
import com.example.product.dto.ProductRequestDto;
import com.example.product.dto.ProductResponseDto;
import com.example.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para el recurso Productos.
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "Operaciones CRUD para productos")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Crear un nuevo producto")
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto created = productService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar un producto existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable("id")
            @NotNull(message = "El id del producto es obligatorio")
            @Positive(message = "El id del producto debe ser un número entero positivo")
            Long id,
            @Valid @RequestBody ProductRequestDto requestDto) {

        return ResponseEntity.ok(productService.update(id, requestDto));
    }


    @Operation(summary = "Obtener un producto por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(
            @PathVariable("id")
            @NotNull(message = "El id del producto es obligatorio")
            @Positive(message = "El id del producto debe ser un número entero positivo")
            Long id) {

        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(summary = "Eliminar un producto por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id")
            @NotNull(message = "El id del producto es obligatorio")
            @Positive(message = "El id del producto debe ser un número entero positivo")
            Long id) {

        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar productos con paginación")
    @GetMapping
    public ResponseEntity<PagedResponseDto<ProductResponseDto>> getAll(
            @RequestParam(name = "page", defaultValue = "0")
            @Min(value = 0, message = "El número de página no puede ser negativo") int page,

            @RequestParam(name = "size", defaultValue = "10")
            @Min(value = 1, message = "El tamaño de página debe ser al menos 1")
            @Max(value = 100, message = "El tamaño de página no puede superar 100") int size,

            @RequestParam(name = "sortBy", defaultValue = "id")
            @Size(min = 1, max = 50, message = "El campo de ordenamiento no es válido") String sortBy,

            @RequestParam(name = "sortDir", defaultValue = "asc")
            @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE,
                    message = "El orden debe ser 'asc' o 'desc'") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(productService.getAll(pageable));
    }
}
