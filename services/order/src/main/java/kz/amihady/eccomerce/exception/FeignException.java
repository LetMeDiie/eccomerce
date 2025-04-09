package kz.amihady.eccomerce.exception;

public class FeignException extends RuntimeException{

    public FeignException(String message) {
        super(message);
    }
}
