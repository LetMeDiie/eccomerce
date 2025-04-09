package kz.amihady.eccomerce.service;

import jakarta.annotation.PostConstruct;
import kz.amihady.eccomerce.customer.CustomerResponse;
import kz.amihady.eccomerce.customer.CustomerServiceClient;
import kz.amihady.eccomerce.exception.BusinessException;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.kafka.producer.OrderKafkaProducer;
import kz.amihady.eccomerce.kafka.producer.event.OrderCanceledEvent;
import kz.amihady.eccomerce.order.OrderCommandService;
import kz.amihady.eccomerce.order.OrderStatus;
import kz.amihady.eccomerce.order.entity.Order;
import kz.amihady.eccomerce.order.mapper.OrderMapper;
import kz.amihady.eccomerce.order.repo.OrderRepository;
import kz.amihady.eccomerce.order.request.OrderRequest;
import kz.amihady.eccomerce.product.ProductServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

public class OrderCommandServiceTest {
    @InjectMocks
    private OrderCommandService orderCommandService;

    @Mock
    private ProductServiceClient productServiceClient;

    @Mock
    private CustomerServiceClient customerServiceClient;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderKafkaProducer orderKafkaProducer;

    private UUID orderId;
    private UUID productId;
    private UUID customerId;

    @PostConstruct
    public void setUp(){
        orderId = UUID.randomUUID();
        productId = UUID.randomUUID();
        customerId = UUID.randomUUID();
    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully() {
        // Given
        Long quantity = 5L;
        OrderRequest orderRequest = new OrderRequest(customerId, productId, quantity);
        BigDecimal productPrice = new BigDecimal("100");
        BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));

        CustomerResponse customerResponse = new CustomerResponse(customerId, "A", "B", "C");

        Order mockOrder = Order.builder()
                .id(orderId)
                .productId(productId)
                .customerId(customerId)
                .orderStatus(OrderStatus.PENDING)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();

        when(productServiceClient.getProductPrice(productId)).thenReturn(productPrice);
        when(customerServiceClient.findById(customerId)).thenReturn(customerResponse);
        when(orderMapper.toOrder(orderRequest, OrderStatus.PENDING, totalPrice)).thenReturn(mockOrder);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        UUID resultOrderId = orderCommandService.createOrder(orderRequest);


        assertEquals(OrderStatus.PENDING,mockOrder.getOrderStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderKafkaProducer, times(1)).sendOrderReserveRequestEvent(any());
    }

    @Test
    void cancelOrder_ShouldCancelOrder_WhenOrderIsPlaced(){
        var order = Order.builder()
                .orderStatus(OrderStatus.PLACED)
                .build();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        orderCommandService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCELED,order.getOrderStatus());

        verify(orderKafkaProducer,times(1))
                .sendOrderCanceledEvent(any(OrderCanceledEvent.class));
        verify(orderRepository,times(1))
                .save(order);
    }

    @Test
    void cancelOrder_ShouldThrowException_WhenOrderIsNotPlaced() {
        var order = Order.builder()
                        .orderStatus(OrderStatus.PENDING)
                        .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        BusinessException exception = assertThrows(BusinessException.class, () -> orderCommandService.cancelOrder(orderId));

        assertTrue(exception.getMessage().contains("Невозможно отменить заказ"));

        verify(orderKafkaProducer, never()).sendOrderCanceledEvent(any());
        verify(orderRepository,never()).save(any());
    }

    @Test
    void cancelOrder_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderCommandService.cancelOrder(orderId));
        verify(orderKafkaProducer, never()).sendOrderCanceledEvent(any());
    }

}
