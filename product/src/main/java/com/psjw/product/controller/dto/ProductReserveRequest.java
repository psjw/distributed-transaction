package com.psjw.product.controller.dto;

import com.psjw.product.appliaction.dto.ProductReserveCommand;
import java.util.List;

public record ProductReserveRequest(
        String requestId,
        List<ReserveItem> items
) {

    public ProductReserveCommand toCommand(){
        return new ProductReserveCommand(
                requestId,
                items.stream()
                        .map(
                                item -> new ProductReserveCommand.ReserveItem(item.productId,
                                        item.reserveQuantity)
                        ).toList()
        );
    }

    public record ReserveItem(
        Long productId,
        Long reserveQuantity
    ){}
}
