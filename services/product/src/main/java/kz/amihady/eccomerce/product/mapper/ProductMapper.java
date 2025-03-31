package kz.amihady.eccomerce.product.mapper;


import kz.amihady.eccomerce.image.mapper.ImageMapper;
import kz.amihady.eccomerce.product.entity.Product;
import kz.amihady.eccomerce.product.request.CreateRequest;
import kz.amihady.eccomerce.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ImageMapper imageMapper;

    public Product toProduct(CreateRequest request){
        return Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();
    }

    public ProductResponse fromProduct(Product product) {
        return new ProductResponse(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImages().stream()
                        .map(imageMapper::fromImage)
                        .toList()
        );
    }
}
