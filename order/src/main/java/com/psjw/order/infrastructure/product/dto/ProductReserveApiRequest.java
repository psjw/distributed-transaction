package com.psjw.order.infrastructure.product.dto;

import java.util.List;

public record ProductReserveApiRequest(
        String requestId,
        List<ReserveItem> items
) {


    public record ReserveItem(
        Long productId,
        Long reserveQuantity
    ){}
}
