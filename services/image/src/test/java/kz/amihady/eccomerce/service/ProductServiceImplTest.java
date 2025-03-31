package kz.amihady.eccomerce.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.repo.ProductRepository;
import kz.amihady.eccomerce.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl productService;

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
    }


    @Test
    void testCreateProduct() {
        Product product = new Product(id);

        when(repository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.create(id);

        assertNotNull(createdProduct);
        assertEquals(id, createdProduct.getId());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void testExistsById_WhenProductExists() {
        when(repository.existsById(id)).thenReturn(true);

        boolean exists = productService.existsById(id);

        assertTrue(exists);
        verify(repository, times(1)).existsById(id);
    }

    @Test
    void testExistsById_WhenProductDoesNotExist() {
        when(repository.existsById(id)).thenReturn(false);

        boolean exists = productService.existsById(id);

        assertFalse(exists);
        verify(repository, times(1)).existsById(id);
    }

    @Test
    void testDelete_WhenProductExists() {
        when(repository.existsById(id)).thenReturn(true);

        productService.delete(id);

        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDelete_WhenProductDoesNotExist() {
        when(repository.existsById(id)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> productService.delete(id));
        assertEquals("Нет продукта с id:" + id.toString(), thrown.getMessage());

        verify(repository, times(1)).existsById(id);
        verify(repository, times(0)).deleteById(id);
    }
}