package com.mars.workflow.dto;

import java.time.LocalDate;

public class SolicitacaoCreditoDTO {

    private Long id;
    private Long clienteId;
    private Double valorSolicitado;
    private Boolean aprovado;
    private Double limiteConcedido;
    private String parecer;
    private LocalDate dataSolicitacao;

    // Construtores
    public SolicitacaoCreditoDTO() {}

    public SolicitacaoCreditoDTO(Long id, Long clienteId, Double valorSolicitado,
                                 Boolean aprovado, Double limiteConcedido,
                                 String parecer, LocalDate dataSolicitacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.valorSolicitado = valorSolicitado;
        this.aprovado = aprovado;
        this.limiteConcedido = limiteConcedido;
        this.parecer = parecer;
        this.dataSolicitacao = dataSolicitacao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Double getValorSolicitado() { return valorSolicitado; }
    public void setValorSolicitado(Double valorSolicitado) { this.valorSolicitado = valorSolicitado; }

    public Boolean getAprovado() { return aprovado; }
    public void setAprovado(Boolean aprovado) { this.aprovado = aprovado; }

    public Double getLimiteConcedido() { return limiteConcedido; }
    public void setLimiteConcedido(Double limiteConcedido) { this.limiteConcedido = limiteConcedido; }

    public String getParecer() { return parecer; }
    public void setParecer(String parecer) { this.parecer = parecer; }

    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
}
