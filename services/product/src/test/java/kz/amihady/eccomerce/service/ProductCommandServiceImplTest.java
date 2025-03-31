package kz.amihady.eccomerce.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.kafka.product.ProductKafkaProducer;
import kz.amihady.eccomerce.kafka.product.event.ProductCreatedEvent;
import kz.amihady.eccomerce.kafka.product.event.ProductDeletedEvent;
import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.mapper.ProductMapper;
import kz.amihady.eccomerce.product.repo.ProductRepository;
import kz.amihady.eccomerce.product.request.CreateRequest;
import kz.amihady.eccomerce.product.request.UpdateRequest;
import kz.amihady.eccomerce.product.response.ProductUpdateResponse;
import kz.amihady.eccomerce.product.service.ProductUpdater;
import kz.amihady.eccomerce.product.service.impl.ProductCommandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProductCommandServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductKafkaProducer productKafkaProducer;

    @Mock
    private ProductUpdater productUpdater;

    @InjectMocks
    private ProductCommandServiceImpl productService;



    private CreateRequest createRequest;
    private Product product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        createRequest = new CreateRequest("Гитара", "Классическая гитара", new BigDecimal("500.00"), 10);

        product = new Product();
        product.setId(productId);
        product.setName(createRequest.name());
        product.setDescription(createRequest.description());
        product.setPrice(createRequest.price());
    }

    @Test
    void create_ShouldSaveProductAndSendEvent() {

        when(productMapper.toProduct(createRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        UUID result = productService.create(createRequest);

        assertNotNull(result);
        assertEquals(product.getId(), result);
        verify(productRepository).save(product);
        verify(productKafkaProducer).sendProductCreatedEvent(new ProductCreatedEvent(product.getId(), createRequest.quantity()));
    }

    @Test
    void delete_ShouldRemoveProductAndSendEvent() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.delete(productId);

        verify(productRepository).delete(product);
        verify(productKafkaProducer).sendProductDeletedEvent(new ProductDeletedEvent(productId));
    }

    @Test
    void delete_ShouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> productService.delete(productId));
        assertEquals("Продукт не найден.", exception.getMessage());

        verify(productRepository, never()).delete(any());
        verify(productKafkaProducer, never()).sendProductDeletedEvent(any());
    }

    @Test
    void update_ShouldUpdateProductSuccessfully() {
        UpdateRequest updateRequest = new UpdateRequest("Новая гитара", "Обновленное описание", new BigDecimal("600.00"));
        ProductUpdateResponse expectedResponse = new ProductUpdateResponse(productId, updateRequest.name(), updateRequest.description(), updateRequest.price());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productUpdater.updateProduct(updateRequest, product)).thenReturn(expectedResponse);

        ProductUpdateResponse response = productService.update(productId, updateRequest);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(productUpdater).updateProduct(updateRequest, product);
    }

    @Test
    void update_ShouldThrowExceptionWhenProductNotFound() {
        UpdateRequest updateRequest = new UpdateRequest("Новая гитара", "Обновленное описание", new BigDecimal("600.00"));

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> productService.update(productId, updateRequest));
        assertEquals("Продукт не найден", exception.getMessage());

        verify(productUpdater, never()).updateProduct(any(), any());
    }
}

