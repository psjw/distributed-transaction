package com.psjw.order.controller;

import com.psjw.order.application.OrderService;
import com.psjw.order.application.dto.CreateOrderResult;
import com.psjw.order.controller.dto.CreateOrderRequest;
import com.psjw.order.controller.dto.CreateOrderResponse;
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
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request){
        CreateOrderResult result = orderService.createOrder(request.toCommand());
        return new CreateOrderResponse(result.orderId());
    }
}
