package com.psjw.order.application;

import com.psjw.order.application.dto.CreateOrderCommand;
import com.psjw.order.application.dto.CreateOrderResult;
import com.psjw.order.application.dto.OrderDto;
import com.psjw.order.application.dto.PlaceOrderCommand;
import com.psjw.order.domain.Order;
import com.psjw.order.domain.OrderItem;
import com.psjw.order.infrastructure.OrderItemRepository;
import com.psjw.order.infrastructure.OrderRepository;
import com.psjw.order.infrastructure.kafka.OrderPlacedProducer;
import com.psjw.order.infrastructure.kafka.dto.OrderPlacedEvent;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderPlacedProducer orderPlacedProducer;


    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            OrderPlacedProducer orderPlacedProducer) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderPlacedProducer = orderPlacedProducer;
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

    @Transactional
    public void placeOrder(PlaceOrderCommand command) {
        Order order = orderRepository.findById(command.orderId()).orElseThrow();
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        order.request();
        orderRepository.save(order);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                orderPlacedProducer.send(
                        new OrderPlacedEvent(
                                command.orderId(),
                                orderItems.stream()
                                        .map(orderItem -> new OrderPlacedEvent.ProductInfo(
                                                orderItem.getProductId(),
                                                orderItem.getQuantity()
                                        )).toList()
                        )

                );

            }
        });
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

    public Order.OrderStatus getStatus(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        return order.getStatus();
    }
}

