package com.buyease.service;

import com.buyease.dto.CreateProductRequest;
import com.buyease.dto.ProductResponse;
import com.buyease.dto.UpdateProductRequest;
import com.buyease.entity.Product;
import com.buyease.exception.DuplicateProductException;
import com.buyease.exception.ProductNotFoundException;
import com.buyease.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;
    private CreateProductRequest createProductRequest;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(100.00))
                .quantity(10)
                .category("Electronics")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        createProductRequest = CreateProductRequest.builder()
                .name("New Product")
                .description("New Description")
                .price(BigDecimal.valueOf(50.00))
                .quantity(5)
                .category("Books")
                .build();
    }

    @Test
    void testCreateProduct_Success() {
        when(productRepository.existsByNameIgnoreCase("New Product")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.createProduct(createProductRequest);

        assertNotNull(response);
        assertEquals(sampleProduct.getId(), response.getId());
        assertEquals(sampleProduct.getName(), response.getName());
        verify(productRepository, times(1)).existsByNameIgnoreCase("New Product");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_DuplicateName() {
        when(productRepository.existsByNameIgnoreCase("New Product")).thenReturn(true);

        assertThrows(DuplicateProductException.class, () -> {
            productService.createProduct(createProductRequest);
        });

        verify(productRepository, times(1)).existsByNameIgnoreCase("New Product");
        verify(productRepository, never()).save(any());
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals(sampleProduct.getId(), response.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<ProductResponse> responses = productService.getAllProducts();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct_Success() {
        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .name("Updated Product")
                .price(BigDecimal.valueOf(150.00))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.existsByNameIgnoreCase("Updated Product")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.updateProduct(1L, updateRequest);

        assertNotNull(response);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(sampleProduct);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        verify(productRepository, never()).delete(any());
    }
}
