package com.mars.workflow.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "solicitacoes_credito")
public class SolicitacaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Associação com o cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Dados da solicitação
    @Column(nullable = false)
    private Double valorSolicitado;

    private Boolean aprovado;          // true = aprovado, false = reprovado, null = pendente
    private Double limiteConcedido;    // valor efetivamente liberado, se aprovado

    @Column(length = 255)
    private String parecer;            // texto opcional: "renda insuficiente", "aprovado automático", etc.

    private LocalDate dataSolicitacao = LocalDate.now();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

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
