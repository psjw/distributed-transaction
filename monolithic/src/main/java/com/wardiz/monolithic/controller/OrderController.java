package com.wardiz.monolithic.controller;

import com.wardiz.monolithic.controller.dto.CreateOrderRequest;
import com.wardiz.monolithic.controller.dto.CreateOrderResponse;
import com.wardiz.monolithic.order.application.dto.CreateOrderResult;
import com.wardiz.monolithic.controller.dto.PlaceOrderRequest;
import com.wardiz.monolithic.order.application.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/order")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderResult result = orderService.createOrder(request.toCreateOrderCommand());

        return new CreateOrderResponse(result.orderId());
    }

    @PostMapping("/order/place")
    public void placeOrder(
            @RequestBody PlaceOrderRequest request
    ) throws InterruptedException {
        orderService.placeOrder(request.toPlaceORderCommand());

    }

}
