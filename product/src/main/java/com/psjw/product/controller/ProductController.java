package com.psjw.product.controller;

import com.psjw.product.appliaction.ProductFacadeService;
import com.psjw.product.appliaction.RedisLockService;
import com.psjw.product.appliaction.dto.ProductReserveResult;
import com.psjw.product.controller.dto.ProductReserveCancelRequest;
import com.psjw.product.controller.dto.ProductReserveConfirmRequest;
import com.psjw.product.controller.dto.ProductReserveRequest;
import com.psjw.product.controller.dto.ProductReserveResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductFacadeService productFacadeService;

    private final RedisLockService redisLockService;

    public ProductController(ProductFacadeService productFacadeService,
            RedisLockService redisLockService) {
        this.productFacadeService = productFacadeService;
        this.redisLockService = redisLockService;
    }


    @PostMapping("/product/reserve")
    public ProductReserveResponse reserve(@RequestBody ProductReserveRequest request) {
        String key = "product:" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());

        if (!acquiredLock) {
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            ProductReserveResult result = productFacadeService.tryReserve(request.toCommand());
            return new ProductReserveResponse(result.totalPrice());
        } finally {
            redisLockService.releaseLock(key);
        }
    }

    @PostMapping("/product/confirm")
    public void confirm(@RequestBody ProductReserveConfirmRequest request) {
        String key = "product:" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());
        if (!acquiredLock) {
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            productFacadeService.confirmReserve(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }

    @PostMapping("/product/cancel")
    public void cancel(@RequestBody ProductReserveCancelRequest request) {
        String key = "product:" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());
        if (!acquiredLock) {
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            productFacadeService.cancelReserve(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }
}
