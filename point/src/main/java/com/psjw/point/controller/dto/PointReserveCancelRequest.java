package com.psjw.point.controller.dto;

import com.psjw.point.application.dto.PointReserveCancelCommand;

public record PointReserveCancelRequest(String requestId) {

    public PointReserveCancelCommand toCommand() {
        return new PointReserveCancelCommand(requestId);
    }
}
