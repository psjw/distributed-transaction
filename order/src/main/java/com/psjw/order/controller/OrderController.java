package com.psjw.order.controller;

import com.psjw.order.application.OrderCoordinator;
import com.psjw.order.application.OrderService;
import com.psjw.order.application.RedisLockService;
import com.psjw.order.application.dto.CreateOrderResult;
import com.psjw.order.controller.dto.CreateOrderRequest;
import com.psjw.order.controller.dto.CreateOrderResponse;
import com.psjw.order.controller.dto.PlaceOrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    private final OrderCoordinator orderCoordinator;

    private final RedisLockService redisLockService;

    public OrderController(OrderService orderService, OrderCoordinator orderCoordinator, RedisLockService redisLockService) {
        this.orderService = orderService;
        this.orderCoordinator = orderCoordinator;
        this.redisLockService = redisLockService;
    }

    @PostMapping("/order")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request){
        CreateOrderResult result = orderService.createOrder(request.toCommand());
        return new CreateOrderResponse(result.orderId());
    }

    @PostMapping("/order/place")
    public void placeOrder(@RequestBody PlaceOrderRequest request){
        String lockKey = "order:" + request.orderId();

        boolean lockAcquired = redisLockService.tryLock(lockKey, request.orderId().toString());

        if(!lockAcquired){
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try{
            orderCoordinator.placeOrder(request.toCommand());
        }finally {
            redisLockService.releaseLock(lockKey);
        }

    }
}
