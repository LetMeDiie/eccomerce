package kz.amihady.eccomerce.service;
import kz.amihady.eccomerce.exception.FileValidationException;
import kz.amihady.eccomerce.image.validation.FileValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileValidatorTest {

    private FileValidator fileValidator;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        fileValidator = new FileValidator();
        file = mock(MultipartFile.class);
    }

    @Test
    void shouldPassValidationForValidJpgFile() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
        when(file.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB

        assertDoesNotThrow(() -> fileValidator.validate(file));
    }

    @Test
    void shouldPassValidationForValidPngFile() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn(MediaType.IMAGE_PNG_VALUE);
        when(file.getSize()).thenReturn(3 * 1024 * 1024L);

        assertDoesNotThrow(() -> fileValidator.validate(file));
    }

    @Test
    void shouldThrowExceptionForEmptyFile() {
        when(file.isEmpty()).thenReturn(true);

        FileValidationException exception = assertThrows(FileValidationException.class, () -> fileValidator.validate(file));
        assertEquals("Файл пустой", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForUnsupportedFileType() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn(MediaType.APPLICATION_PDF_VALUE);

        FileValidationException exception = assertThrows(FileValidationException.class, () -> fileValidator.validate(file));
        assertEquals("Неподдерживаемый формат. Разрешены только JPG, PNG", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForFileSizeExceedingLimit() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
        when(file.getSize()).thenReturn(6 * 1024 * 1024L); // 6MB

        FileValidationException exception = assertThrows(FileValidationException.class, () -> fileValidator.validate(file));
        assertEquals("Файл слишком большой. Максимальный размер: 5MB", exception.getMessage());
    }
}

