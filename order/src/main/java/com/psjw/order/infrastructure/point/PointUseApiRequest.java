package com.psjw.order.infrastructure.point;


public record PointUseApiRequest(
        String requestId,
        Long userId,
        Long amount
) {

}
