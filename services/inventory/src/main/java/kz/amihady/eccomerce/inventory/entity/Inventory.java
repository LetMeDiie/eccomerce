package kz.amihady.eccomerce.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level=AccessLevel.PRIVATE)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false,unique = true)
    UUID productId;

    @Column(nullable = false)
    Long inStock;

    @Column(nullable = false)
    Long reserved;
}
