package com.mars.workflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreditRequestPayload(
        @NotBlank(message = "Informe o CPF")
        @Size(min = 11, max = 11, message = "CPF deve conter 11 dígitos")
        String cpf,

        @NotBlank(message = "Informe o nome")
        String nome,

        @NotNull(message = "Informe a renda mensal")
        @Positive(message = "Renda mensal deve ser maior que zero")
        Double rendaMensal,

        @NotNull(message = "Informe o valor solicitado")
        @Positive(message = "Valor solicitado deve ser maior que zero")
        Double valorSolicitado,

        String observacoes
) {
}
