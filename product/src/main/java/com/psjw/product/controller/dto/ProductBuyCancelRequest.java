package com.psjw.product.controller.dto;

import com.psjw.product.appliaction.dto.ProductBuyCancelCommand;

public record ProductBuyCancelRequest (String requestId){

    public ProductBuyCancelCommand toCommand(){
        return new ProductBuyCancelCommand(requestId);
    }
}
