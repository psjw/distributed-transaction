package com.psjw.monolithic.controller;

import com.psjw.monolithic.controller.dto.CreateOrderRequest;
import com.psjw.monolithic.controller.dto.CreateOrderResponse;
import com.psjw.monolithic.order.application.RedisLockService;
import com.psjw.monolithic.order.application.dto.CreateOrderResult;
import com.psjw.monolithic.controller.dto.PlaceOrderRequest;
import com.psjw.monolithic.order.application.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final RedisLockService redisLockService;


    public OrderController(OrderService orderService, RedisLockService redisLockService) {
        this.orderService = orderService;
        this.redisLockService = redisLockService;
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
        String key = "order:monolithic:" + request.orderId();
        boolean acquiredLock = redisLockService.tryLock(key, request.orderId().toString());

        if(!acquiredLock){
            throw new RuntimeException("락 획득에 실패햐였습니다.");
        }

        try{
            orderService.placeOrder(request.toPlaceORderCommand());
        }finally {
            redisLockService.releaseLock(key);
        }

    }

}
