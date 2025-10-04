package com.wardiz.monolithic.order.application;

import com.wardiz.monolithic.order.application.dto.PlaceOrderCommand;
import com.wardiz.monolithic.order.domain.Order;
import com.wardiz.monolithic.order.domain.OrderItem;
import com.wardiz.monolithic.order.infrastructure.OrderItemRepository;
import com.wardiz.monolithic.order.infrastructure.OrderRepository;
import com.wardiz.monolithic.point.application.PointService;
import com.wardiz.monolithic.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PointService pointService;

    public OrderService(OrderItemRepository orderItemRepository, OrderRepository orderRepository,
            ProductService productService, PointService pointService) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.pointService = pointService;
    }


    @Transactional
    public void placeOrder(PlaceOrderCommand command) {
        Order order = orderRepository.save(new Order());
        Long totalPrice = 0L;
        for (PlaceOrderCommand.OrderItem item : command.orderItems()) {
            OrderItem orderItem = new OrderItem(order.getId(), item.productId(), item.quantity());
            orderItemRepository.save(orderItem);

            Long price = productService.buy(item.productId(), item.quantity());
            totalPrice += price;
        }

        pointService.use(1L, totalPrice);
    }
}
