package com.psjw.product.controller;

import com.psjw.product.appliaction.ProductService;
import com.psjw.product.appliaction.RedisLockService;
import com.psjw.product.appliaction.dto.ProductReserveResult;
import com.psjw.product.controller.dto.ProductReserveRequest;
import com.psjw.product.controller.dto.ProductReserveResponse;
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

    @PostMapping("/product/reserve")
    public ProductReserveResponse reserve(@RequestBody ProductReserveRequest request) {
        String key = "product:" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());

        if(!acquiredLock){
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try{
            ProductReserveResult result = productService.tryReserve(request.toCommand());
            return new ProductReserveResponse(result.totalPrice());
        }finally {
            redisLockService.releaseLock(key);
        }
    }
}
