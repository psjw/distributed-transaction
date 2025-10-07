package com.psjw.product.controller.dto;

import com.psjw.product.appliaction.dto.ProductReserveCancelCommand;

public record ProductReserveCancelRequest(String requestId) {
    public ProductReserveCancelCommand toCommand() {
        return new ProductReserveCancelCommand(requestId);
    }
}
