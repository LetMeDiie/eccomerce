package kz.amihady.eccomerce.order.mapper;

import kz.amihady.eccomerce.order.OrderStatus;
import kz.amihady.eccomerce.order.entity.Order;
import kz.amihady.eccomerce.order.request.OrderRequest;
import kz.amihady.eccomerce.order.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMapper {


    public Order toOrder(OrderRequest request , OrderStatus orderStatus, BigDecimal totalPrice){
        return Order.builder()
                .productId(request.productId())
                .customerId(request.customerId())
                .totalPrice(totalPrice)
                .orderStatus(orderStatus)
                .quantity(request.quantity())
                .build();
    }

    public OrderResponse fromOrder(Order order){
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                order.getCreatedAt()
        );
    }
}
