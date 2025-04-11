package kz.amihady.eccomerce.notification.mapper;

import kz.amihady.eccomerce.notification.entity.Notification;
import kz.amihady.eccomerce.notification.response.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse fromNotification(Notification notification){
        return new NotificationResponse(
                notification.getId(),
                notification.getName(),
                Constants.ORDER_URL+notification.getOrderId()+"/"+notification.getCustomerId(),
                Constants.PRODUCT_URL+notification.getProductId(),
                notification.getMessage()
        );
    }
}
