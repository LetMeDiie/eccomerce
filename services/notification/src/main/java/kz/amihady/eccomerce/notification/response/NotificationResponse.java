package kz.amihady.eccomerce.notification.response;

public record NotificationResponse(
        String id ,
        String name,
        String orderLink,
        String productLink,
        String message
) {
}
