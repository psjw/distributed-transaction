package com.wardiz.monolithic.controller;

import com.wardiz.monolithic.controller.dto.PlaceOrderRequest;
import com.wardiz.monolithic.order.application.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/place")
    public void placeOrder(
            @RequestBody PlaceOrderRequest request
    ){
        orderService.placeOrder(request.toPlaceORderCommand());
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
