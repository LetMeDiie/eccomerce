package kz.amihady.eccomerce.image.validation;


import kz.amihady.eccomerce.exception.FileValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Slf4j
public class FileValidator {

    private static final List<String> ALLOWED_TYPES = List.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public void validate(MultipartFile file) {
        log.info("Начало валидации переданного файла");
        if (file == null || file.isEmpty()) {
            log.warn("Попытка загрузить пустой файл");
            throw new FileValidationException("Файл пустой");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            log.warn("Неподдерживаемый формат: {}. Ожидался JPG или PNG", file.getContentType());
            throw new FileValidationException("Неподдерживаемый формат. Разрешены только JPG, PNG");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            log.warn("Файл слишком большой: {} байт. Максимальный размер: 5MB", file.getSize());
            throw new FileValidationException("Файл слишком большой. Максимальный размер: 5MB");
        }

        log.info("Файл успешно прошел валидацию");
    }
}
