package com.psjw.product.controller.dto;

import com.psjw.product.appliaction.dto.ProductReserveConfirmCommand;

public record ProductReserveConfirmRequest(
        String requestId
) {
    public ProductReserveConfirmCommand toCommand(){
        return new ProductReserveConfirmCommand(requestId);
    }
}
