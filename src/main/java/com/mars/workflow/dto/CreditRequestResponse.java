package com.mars.workflow.dto;

public record CreditRequestResponse(
        Long clienteId,
        String processInstanceId,
        String status
) {
}
