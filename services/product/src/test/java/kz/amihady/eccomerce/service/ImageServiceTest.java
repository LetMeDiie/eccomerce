package kz.amihady.eccomerce.service;


import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.image.entity.Image;
import kz.amihady.eccomerce.image.repo.ImageRepository;
import kz.amihady.eccomerce.image.request.ImageRequest;
import kz.amihady.eccomerce.image.service.ImageService;
import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.service.ProductQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.ArrayList;


@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository repository;

    @Mock
    private ProductQueryService productQueryService;

    @InjectMocks
    private ImageService imageService;

    private UUID imageId;
    private ImageRequest request;
    private Product product;
    private Image image;

    @BeforeEach
    void setUp() {
        imageId = UUID.randomUUID();
        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setImages(new ArrayList<>());

        request = new ImageRequest(imageId, product.getId(), "http://example.com/image.jpg");
        image = new Image();
        image.setId(imageId);
        image.setProduct(product);
        image.setImageUrl(request.imageUrl());
    }

    @Test
    @Transactional
    void addImage_success() {
        when(productQueryService.findById(request.productId())).thenReturn(product);
        when(repository.save(any(Image.class))).thenReturn(image);

        imageService.addImage(request);

        verify(repository, times(1)).save(any(Image.class));
    }

    @Test
    @Transactional
    void deleteImage_success() {
        when(repository.findById(imageId)).thenReturn(Optional.of(image));

        imageService.deleteImage(imageId);

        verify(repository, times(1)).delete(image);
    }

    @Test
    @Transactional
    void deleteImage_notFound_throwsException() {
        when(repository.findById(imageId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> imageService.deleteImage(imageId));

        assertEquals("Изображение не найдено", exception.getMessage());
        verify(repository, never()).delete(any(Image.class));
    }
}
