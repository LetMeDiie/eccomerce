package kz.amihady.eccomerce.service;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.mapper.ProductMapper;
import kz.amihady.eccomerce.product.repo.ProductRepository;
import kz.amihady.eccomerce.product.response.ProductResponse;
import kz.amihady.eccomerce.product.service.impl.ProductQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductQueryServiceImpl productQueryService;

    private UUID productId;
    private Product product;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = new Product(productId, "Guitar", "Acoustic guitar", new BigDecimal("100.00"), new ArrayList<>());
        productResponse = new ProductResponse("Guitar", "Acoustic guitar", new BigDecimal("100.00"), new ArrayList<>());
    }

    @Test
    void findProduct_ShouldReturnProductResponse_WhenProductExists() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.fromProduct(product)).thenReturn(productResponse);

        ProductResponse result = productQueryService.findProduct(productId);

        assertNotNull(result);
        assertEquals("Guitar", result.name());
        verify(productRepository).findById(productId);
        verify(productMapper).fromProduct(product);
    }

    @Test
    void findProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productQueryService.findProduct(productId));
        verify(productRepository).findById(productId);
    }

    @Test
    void findAll_ShouldReturnProductResponses() {
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productPage);
        when(productMapper.fromProduct(product)).thenReturn(productResponse);

        List<ProductResponse> result = productQueryService.findAll(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Guitar", result.get(0).name());
        verify(productRepository).findAll(any(PageRequest.class));
        verify(productMapper).fromProduct(product);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoProductsAvailable() {
        Page<Product> emptyPage = Page.empty();

        when(productRepository.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        List<ProductResponse> result = productQueryService.findAll(1);

        assertTrue(result.isEmpty());
        verify(productRepository).findAll(any(PageRequest.class));
    }
}

