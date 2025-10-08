package com.psjw.point.controller.dto;

import com.psjw.point.application.dto.PointUseCancelCommand;

public record PointUseCancelRequest(String requestId) {

    public PointUseCancelCommand toCommand() {
        return new PointUseCancelCommand(requestId);
    }
}
