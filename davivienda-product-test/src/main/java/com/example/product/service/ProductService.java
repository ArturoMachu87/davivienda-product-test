package com.example.product.service;

import com.example.product.dto.PagedResponseDto;
import com.example.product.dto.ProductRequestDto;
import com.example.product.dto.ProductResponseDto;
import org.springframework.data.domain.Pageable;

/**
 * author Arturo Machuca
 */
public interface ProductService {

    ProductResponseDto create(ProductRequestDto requestDto);

    ProductResponseDto update(Long id, ProductRequestDto requestDto);

    void delete(Long id);

    ProductResponseDto getById(Long id);

    PagedResponseDto<ProductResponseDto> getAll(Pageable pageable);
}
