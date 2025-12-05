package com.example.product.controller;

import com.example.product.dto.PagedResponseDto;
import com.example.product.dto.ProductRequestDto;
import com.example.product.dto.ProductResponseDto;
import com.example.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para ProductController usando MockMvc.
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void create_ShouldReturnCreated() throws Exception {
        ProductRequestDto request = new ProductRequestDto();
        request.setName("Prod 1");
        request.setPrice(new BigDecimal("10.00"));
        request.setDescription("Desc");
        request.setActive(true);

        ProductResponseDto response = new ProductResponseDto();
        response.setId(1L);
        response.setName("Prod 1");
        response.setPrice(new BigDecimal("10.00"));

        when(productService.create(any(ProductRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Prod 1"));
    }

    @Test
    void getAll_ShouldReturnOk() throws Exception {
        ProductResponseDto prod = new ProductResponseDto();
        prod.setId(1L);
        prod.setName("Prod 1");
        prod.setPrice(new BigDecimal("10.00"));

        PagedResponseDto<ProductResponseDto> page = new PagedResponseDto<>(
                List.of(prod), 0, 10, 1, 1);

        when(productService.getAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }
}
