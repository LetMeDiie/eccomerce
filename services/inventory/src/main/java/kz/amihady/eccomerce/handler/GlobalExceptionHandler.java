package kz.amihady.eccomerce.handler;

import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handle (EntityNotFoundException ex){
        return ResponseEntity
                .status(404)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handle (BusinessException ex){
        return ResponseEntity
                .status(400)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");

        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getField())
                    .append(" ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        return ResponseEntity
                .status(400)
                .body(errorMessage.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        String errorMessage = "Произошла непредвиденная ошибка: " + ex.getMessage();
        return ResponseEntity
                .status(500)
                .body(errorMessage);
    }
}
