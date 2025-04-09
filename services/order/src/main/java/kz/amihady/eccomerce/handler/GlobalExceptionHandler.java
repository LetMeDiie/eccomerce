package kz.amihady.eccomerce.handler;

import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.exception.FeignException;
import kz.amihady.eccomerce.exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.ServiceUnavailableException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handle(EntityNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handle(ForbiddenException exception){
        return  ResponseEntity
                .status(403)
                .body(exception.getMessage());

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handle(BusinessException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Ошибка валидации: ");

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(String.format("%s: %s; ", error.getField(), error.getDefaultMessage()));
        }

        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<String> handle(ServiceUnavailableException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handle(FeignException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
