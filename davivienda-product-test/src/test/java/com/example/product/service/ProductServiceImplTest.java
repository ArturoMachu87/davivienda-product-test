package com.example.product.service;

import com.example.product.dto.ProductRequestDto;
import com.example.product.dto.ProductResponseDto;
import com.example.product.exception.ProductNotFoundException;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductServiceImpl.
 */
class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void create_ShouldPersistProduct() {
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Producto 1");
        dto.setPrice(new BigDecimal("10.00"));
        dto.setDescription("Desc");
        dto.setActive(true);

        Product saved = new Product();
        saved.setId(1L);
        saved.setName("Producto 1");
        saved.setPrice(dto.getPrice());

        when(productRepository.save(ArgumentMatchers.any(Product.class))).thenReturn(saved);

        ProductResponseDto response = productService.create(dto);

        assertNotNull(response.getId());
        assertEquals("Producto 1", response.getName());
        verify(productRepository, times(1)).save(ArgumentMatchers.any(Product.class));
    }

    @Test
    void getById_NotFound_ShouldThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getById(1L));
    }

    @Test
    void getAll_ShouldReturnPagedList() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Prod");
        product.setPrice(new BigDecimal("5.00"));

        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(page);

        var result = productService.getAll(PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
    }
}
