package com.wardiz.monolithic.order.application;

import com.wardiz.monolithic.order.application.dto.CreateOrderResult;
import com.wardiz.monolithic.order.application.dto.CreateOrderCommand;
import com.wardiz.monolithic.order.application.dto.PlaceOrderCommand;
import com.wardiz.monolithic.order.domain.Order;
import com.wardiz.monolithic.order.domain.Order.OrderStatus;
import com.wardiz.monolithic.order.domain.OrderItem;
import com.wardiz.monolithic.order.infrastructure.OrderItemRepository;
import com.wardiz.monolithic.order.infrastructure.OrderRepository;
import com.wardiz.monolithic.point.application.PointService;
import com.wardiz.monolithic.product.application.ProductService;
import java.util.List;
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
    public void placeOrder(PlaceOrderCommand command) throws InterruptedException {

        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new RuntimeException("주문정보가 존재하지 않습니다."));

        if(order.getStatus() == OrderStatus.COMPLETE){
            return;
        }

        Long totalPrice = 0L;

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        for (OrderItem item : orderItems) {
            Long price = productService.buy(item.getProductId(), item.getQuantity());
            totalPrice += price;
        }

        pointService.use(1L, totalPrice);

        order.complete();
        orderRepository.save(order);

        System.out.println("결제 완료!!!");
        Thread.sleep(3000);
    }
}
