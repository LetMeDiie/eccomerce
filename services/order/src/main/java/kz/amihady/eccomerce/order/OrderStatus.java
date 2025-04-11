package kz.amihady.eccomerce.order;

public enum OrderStatus{
    PENDING("Ваш заказ в ожидании."),
    PLACED("Ваш заказ размещён."),
    PAID("Ваш заказ оплачен."),
    CANCELED("Ваш заказ отменён."),
    FAILED("Не удалось обработать ваш заказ.");

    private final String message;

    OrderStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}