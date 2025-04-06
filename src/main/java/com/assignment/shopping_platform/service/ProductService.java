package com.assignment.shopping_platform.service;

import com.assignment.shopping_platform.dto.ProductDto;
import com.assignment.shopping_platform.dto.ProductUpdateDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.factory.ProductFactory;
import com.assignment.shopping_platform.repositroy.ProductRepository;
import com.assignment.shopping_platform.shared.Page;
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
    private static final Sort SORTING_ORDER = Sort.by(asc("name"));

    private final ProductRepository productRepository;
    private final ProductFactory productFactory;

    public ProductDto save(ProductUpdateDto productUpdateDto) {
        return ProductDto.from(productRepository.save(productFactory.create(productUpdateDto)));
    }

    public ProductDto update(UUID productId, ProductUpdateDto productUpdateDto) {
        return productRepository.findByExternalId(productId)
                .map(product -> productFactory.update(product, productUpdateDto))
                .map(productRepository::save)
                .map(ProductDto::from)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public List<ProductDto> query(Page page) {
        return productRepository.findAll(pageRequest(page))
                .map(ProductDto::from)
                .get()
                .toList();
    }

    private static PageRequest pageRequest(Page page) {
        return PageRequest.of(page.pageNumber(), page.pageSize(), SORTING_ORDER);
    }
}
