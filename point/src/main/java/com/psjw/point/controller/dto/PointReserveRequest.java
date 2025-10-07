package com.psjw.point.controller.dto;

import com.psjw.point.application.dto.PointReserveCommand;

public record PointReserveRequest(
        String requestId,
        Long userId,
        Long reserveAmount
) {

    public PointReserveCommand toCommand() {
        return new PointReserveCommand(requestId, userId, reserveAmount);
    }

}
