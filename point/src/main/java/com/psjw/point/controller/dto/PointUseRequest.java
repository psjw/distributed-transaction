package com.psjw.point.controller.dto;

import com.psjw.point.application.dto.PointUseCommand;

public record PointUseRequest(
        String requestId,
        Long userId,
        Long amount
) {

    public PointUseCommand toCommand() {
        return new PointUseCommand(requestId, userId, amount);
    }
}
