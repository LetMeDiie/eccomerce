package kz.amihady.eccomerce.config;


import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.exception.FeignException;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = "";
        try {
            if (response.body() != null) {
                responseBody = Util.toString(response.body().asReader());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.status() == 404) {
            return new EntityNotFoundException(responseBody);
        }

        if (response.status() == 400) {
            return new BusinessException(responseBody);
        }

        if (response.status() == 503) {
            return new ServiceUnavailableException("Сервис недоступен: " + responseBody);
        }
        return new FeignException("Произошла ошибка при обращении к внешнему сервису: " + responseBody);

    }
}
