package kz.amihady.eccomerce.notification.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {
    @Id
    private String id;
    private String name;
    private String message;
    private String email;
    private UUID orderId;
    private UUID productId;
    private UUID customerId;
}
