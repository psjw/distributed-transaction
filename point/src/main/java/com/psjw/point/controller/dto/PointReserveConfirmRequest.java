package com.psjw.point.controller.dto;

import com.psjw.point.application.dto.PointReserveConfirmCommand;

public record PointReserveConfirmRequest (String requestId){

    public PointReserveConfirmCommand toCommand(){
        return new PointReserveConfirmCommand(requestId);
    }
}
