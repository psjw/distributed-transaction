package com.psjw.order.controller;


import com.psjw.order.appliaction.OrderCoordinator;
import com.psjw.order.appliaction.OrderService;
import com.psjw.order.appliaction.RedisLockService;
import com.psjw.order.appliaction.dto.CreateOrderResult;
import com.psjw.order.controller.dto.CreateOrderRequest;
import com.psjw.order.controller.dto.CreateOrderResponse;
import com.psjw.order.controller.dto.PlaceOrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final RedisLockService redisLockService;
    private final OrderCoordinator orderCoordinator;


    public OrderController(OrderService orderService, RedisLockService redisLockService,
            OrderCoordinator orderCoordinator) {
        this.orderService = orderService;
        this.redisLockService = redisLockService;
        this.orderCoordinator = orderCoordinator;
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
            orderCoordinator.placeOrder(request.toCommand());
        }finally {
            redisLockService.releaseLock(key);
        }
    }

}
