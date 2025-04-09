package kz.amihady.eccomerce.order;

import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.exception.ForbiddenException;
import kz.amihady.eccomerce.order.entity.Order;
import kz.amihady.eccomerce.order.mapper.OrderMapper;
import kz.amihady.eccomerce.order.repo.OrderRepository;
import kz.amihady.eccomerce.order.response.OrderResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class OrderQueryService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;


    public OrderResponse findById(UUID id, UUID customerId) {
        log.info("Запрос на получение заказа с ID: {}", id);

        var order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Заказ с ID %s не найден в базе данных.", id);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });

        if (!order.getCustomerId().equals(customerId)) {
            log.warn("Попытка доступа к чужому заказу. ID заказа: {}, ID клиента: {}", id, customerId);
            throw new ForbiddenException("Ошибка: у вас нет доступа к данному заказу.");
        }

        log.info("Заказ с ID: {} успешно найден.", id);

        log.debug("Сгенерирована ссылка на продукт с ID: {}", order.getProductId());

        OrderResponse orderResponse = orderMapper.fromOrder(order);
        log.info("Заказ с ID: {} преобразован в OrderResponse.", id);

        return orderResponse;
    }

    public List<OrderResponse> findAll(UUID customerId) {
        log.info("Запрос на получение всех заказов для клиента с ID: {}", customerId);

        List<Order> orders = orderRepository.findByCustomerId(customerId);

        if (orders.isEmpty()) {
            String errorMessage = String.format("Нет заказов для клиента с ID: %s", customerId);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        log.info("Найдено {} заказов для клиента с ID: {}", orders.size(), customerId);

        List<OrderResponse> orderResponses = orders.stream()
                .map(order -> {
                    log.debug("Сгенерирована ссылка на продукт с ID: {}", order.getProductId());
                    return orderMapper.fromOrder(order);
                })
                .collect(Collectors.toList());

        log.info("Все заказы для клиента с ID: {} преобразованы в OrderResponse.", customerId);
        return orderResponses;
    }
}
