package com.psjw.order.appliaction;


import com.psjw.order.appliaction.dto.CreateOrderCommand;
import com.psjw.order.appliaction.dto.CreateOrderResult;
import com.psjw.order.appliaction.dto.OrderDto;
import com.psjw.order.domain.Order;
import com.psjw.order.domain.OrderItem;
import com.psjw.order.infrastructure.OrderItemRepository;
import com.psjw.order.infrastructure.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }


    public OrderDto getOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow();
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);

        return new OrderDto(
                orderItems.stream().map(item -> new OrderDto.OrderItem(item.getProductId(),
                        item.getQuantity())).toList()
        );
    }



    @Transactional
    public CreateOrderResult createOrder(CreateOrderCommand command) {
        Order order = orderRepository.save(new Order());
        List<OrderItem> orderItems = command.orderItems()
                .stream()
                .map(item -> new OrderItem(order.getId(), item.productId(), item.quantity()))
                .toList();

        orderItemRepository.saveAll(orderItems);

        return new CreateOrderResult(order.getId());
    }

    @Transactional
    public void reserve(Long orderId) {
        Order order = orderRepository
                .findById(orderId).orElseThrow();

        order.reserve();
        orderRepository.save(order);
    }

    @Transactional
    public void cancel(Long orderId) {
        Order order = orderRepository
                .findById(orderId).orElseThrow();

        order.cancel();
        orderRepository.save(order);

    }

    @Transactional
    public void confirm(Long orderId) {
        Order order = orderRepository
                .findById(orderId).orElseThrow();

        order.confirm();
        orderRepository.save(order);
    }


    @Transactional
    public void pending(Long orderId) {
        Order order = orderRepository
                .findById(orderId).orElseThrow();

        order.pending();
        orderRepository.save(order);
    }



}
