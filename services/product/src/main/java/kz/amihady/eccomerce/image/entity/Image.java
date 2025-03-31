package kz.amihady.eccomerce.image.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kz.amihady.eccomerce.product.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)

public class Image {

    @Id
    UUID id;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    String imageUrl;
}
