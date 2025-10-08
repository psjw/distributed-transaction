package com.psjw.order.application;

import com.psjw.order.application.dto.CreateOrderCommand;
import com.psjw.order.application.dto.CreateOrderResult;
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
}
