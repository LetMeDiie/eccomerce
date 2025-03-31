package kz.amihady.eccomerce.image.entity;


import jakarta.persistence.*;
import kz.amihady.eccomerce.image.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class Image {
    @Id
    UUID id;

    UUID productId;

    @Enumerated(EnumType.STRING)
    Status status;
}
