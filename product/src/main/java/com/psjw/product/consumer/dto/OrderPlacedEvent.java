package com.psjw.product.consumer.dto;

import java.util.List;

public record OrderPlacedEvent(
        Long orderId,
        List<ProductInfo> productInfos
) {

    public record ProductInfo(
            Long productId,
            Long quantity) {


    }
}
