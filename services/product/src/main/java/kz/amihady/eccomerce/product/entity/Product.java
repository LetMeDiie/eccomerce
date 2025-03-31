package kz.amihady.eccomerce.product.entity;

import jakarta.persistence.*;
import kz.amihady.eccomerce.image.entity.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(generator = "UUID")
    UUID id;

    String name;
    String description;
    BigDecimal price;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Image> images;
}
