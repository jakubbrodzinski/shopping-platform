package com.assignment.shopping_platform.service;

import com.assignment.shopping_platform.dto.ProductDto;
import com.assignment.shopping_platform.dto.ProductUpdateDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.factory.ProductFactory;
import com.assignment.shopping_platform.repositroy.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductFactory productFactory;

    ProductDto create(ProductUpdateDto productUpdateDto) {
        return ProductDto.from(productRepository.save(productFactory.create(productUpdateDto)));
    }

    ProductDto update(UUID productId, ProductUpdateDto productUpdateDto) {
        return productRepository.findByExternalId(productId)
                .map(product -> productFactory.update(product, productUpdateDto))
                .map(productRepository::save)
                .map(ProductDto::from)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    List<ProductDto> query(int pageNumber, int pageSize) {
        return productRepository.findAll(pageRequest(pageNumber, pageSize))
                .map(ProductDto::from)
                .get()
                .toList();
    }

    private static PageRequest pageRequest(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize, Sort.by(asc("name")));
    }
}
