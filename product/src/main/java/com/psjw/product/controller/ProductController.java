package com.psjw.product.controller;

import com.psjw.product.appliaction.ProductService;
import com.psjw.product.appliaction.RedisLockService;
import com.psjw.product.appliaction.dto.ProductBuyCancelResult;
import com.psjw.product.appliaction.dto.ProductBuyResult;
import com.psjw.product.controller.dto.ProductBuyCancelRequest;
import com.psjw.product.controller.dto.ProductBuyCancelResponse;
import com.psjw.product.controller.dto.ProductBuyRequest;
import com.psjw.product.controller.dto.ProductBuyResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService productService;
    private final RedisLockService redisLockService;

    public ProductController(ProductService productService, RedisLockService redisLockService) {
        this.productService = productService;
        this.redisLockService = redisLockService;
    }

    @PostMapping("/product/buy")
    public ProductBuyResponse buy(@RequestBody ProductBuyRequest request){
        String lockKey = "product:orchestration" + request.requestId();

        boolean lockAcquired = redisLockService.tryLock(lockKey, request.requestId());

        if(!lockAcquired){
            System.out.println("락 획득에 실패하였습니다.");
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try{
            ProductBuyResult buyResult = productService.buy(request.toCommand());

            return new  ProductBuyResponse(buyResult.totalPrice());
        }finally {
            redisLockService.releaseLock(lockKey);
        }
    }


    @PostMapping("/product/buy/cancel")
    public ProductBuyCancelResponse cancel(@RequestBody ProductBuyCancelRequest request){
        String lockKey = "product:orchestration" + request.requestId();

        boolean lockAcquired = redisLockService.tryLock(lockKey, request.requestId());

        if(!lockAcquired){
            System.out.println("락 획득에 실패하였습니다.");
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try{
            ProductBuyCancelResult cancelResult = productService.cancel(request.toCommand());

            return new  ProductBuyCancelResponse(cancelResult.totalPrice());
        }finally {
            redisLockService.releaseLock(lockKey);
        }
    }
}
