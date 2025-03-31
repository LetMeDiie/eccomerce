package kz.amihady.eccomerce.image.mapper;

import kz.amihady.eccomerce.image.entity.Image;
import kz.amihady.eccomerce.image.response.ImageResponse;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {

    public ImageResponse fromImage(Image image){
        return new ImageResponse(
                image.getImageUrl()
        );
    }


}
