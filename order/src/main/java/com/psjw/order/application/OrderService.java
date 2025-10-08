package com.psjw.order.application;

import com.psjw.order.application.dto.CreateOrderCommand;
import com.psjw.order.application.dto.CreateOrderResult;
import com.psjw.order.application.dto.OrderDto;
import com.psjw.order.domain.Order;
import com.psjw.order.domain.OrderItem;
import com.psjw.order.infrastructure.OrderItemRepository;
import com.psjw.order.infrastructure.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public CreateOrderResult createOrder(CreateOrderCommand command) {
        Order order = orderRepository.save(new Order());

        List<OrderItem> orderItems = command.items()
                .stream()
                .map(item -> new OrderItem(order.getId(), item.productId(), item.quantity()))
                .toList();

        orderItemRepository.saveAll(orderItems);

        return new CreateOrderResult(order.getId());
    }

    public OrderDto getOrder(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
        return new OrderDto(
                orderItems.stream()
                        .map(item -> new OrderDto.OrderItem(
                                item.getProductId(),
                                item.getQuantity()
                        )).toList()
        );
    }

    @Transactional
    public void request(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        order.request();
        orderRepository.save(order);
    }

    @Transactional
    public void complete(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        order.complete();
        orderRepository.save(order);
    }


    @Transactional
    public void fail(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        order.fail();
        orderRepository.save(order);
    }
}

