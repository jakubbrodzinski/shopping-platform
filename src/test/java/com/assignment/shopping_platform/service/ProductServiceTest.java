package com.assignment.shopping_platform.service;

import com.assignment.shopping_platform.dto.ProductDto;
import com.assignment.shopping_platform.dto.ProductUpdateDto;
import com.assignment.shopping_platform.exception.ProductNotFoundException;
import com.assignment.shopping_platform.repositroy.ProductRepository;
import com.assignment.shopping_platform.repositroy.model.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static com.assignment.shopping_platform.TestFixtures.EUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.joda.money.CurrencyUnit.EUR;

@SpringBootTest
@Transactional
class ProductServiceTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Nested
    class CreateProductTests {
        @Test
        void shouldCreateProduct() {
            var dto = ProductUpdateDto.builder()
                    .name("New Product")
                    .description("Test Description")
                    .price(EUR("3.15"))
                    .build();

            var result = productService.save(dto);

            assertThat(result.name()).isEqualTo("New Product");
            assertThat(result.description()).isEqualTo("Test Description");
            assertThat(result.price()).isEqualTo(EUR("3.15"));
        }

        @Test
        void shouldGenerateUniqueExternalId() {
            var dto = ProductUpdateDto.builder()
                    .name("Product")
                    .description("Test Description")
                    .price(EUR("1.15"))
                    .build();

            var first = productService.save(dto);
            var second = productService.save(dto);

            assertThat(first.id()).isNotEqualTo(second.id());
        }
    }

    @Nested
    class UpdateProductTests {
        @Test
        void shouldUpdateExistingProduct() {
            var exisitngProduct = persistProduct("p-1", "0.13");
            var dto = ProductUpdateDto.builder()
                    .name("Updated Name")
                    .description("Test Description")
                    .price(EUR("3.15"))
                    .build();

            var updated = productService.update(exisitngProduct.getExternalId(), dto);

            assertThat(updated.name()).isEqualTo("Updated Name");
            assertThat(updated.description()).isEqualTo("Test Description");
            assertThat(updated.price()).isEqualTo(EUR("3.15"));
        }

        @Test
        void shouldThrowWhenProductNotFound() {
            var productUpdateDto = ProductUpdateDto.builder()
                    .name("Updated Name")
                    .description("Test Description")
                    .price(EUR("3.15"))
                    .build();


            assertThatThrownBy(() -> productService.update(UUID.randomUUID(), productUpdateDto))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    class QueryProductsTests {
        @Test
        void shouldReturnPaginatedResults() {
            persistProduct("product-1", "0.15");
            persistProduct("product-2", "0.31");

            var results = productService.query(0, 1);

            assertThat(results)
                    .singleElement()
                    .extracting(ProductDto::name)
                    .isEqualTo("product-1");
        }

        @Test
        void shouldReturnEmptyListForInvalidPage() {
            var results = productService.query(999, 10);

            assertThat(results).isEmpty();
        }

        @Test
        void shouldReturnSubSequentPage() {
            persistProduct("product-1", "0.15");
            persistProduct("product-2", "0.31");
            persistProduct("product-3", "0.31");

            var results = productService.query(1, 2);

            assertThat(results)
                    .singleElement()
                    .extracting(ProductDto::name)
                    .isEqualTo("product-3");
        }

    }

    private Product persistProduct(String name, String price) {
        var product = new Product();
        product.setExternalId(UUID.randomUUID());
        product.setName(name);
        product.setDescription("Desc - " + name);
        product.setPrice(new BigDecimal(price));
        product.setCurrency(EUR);
        return productRepository.save(product);
    }
}