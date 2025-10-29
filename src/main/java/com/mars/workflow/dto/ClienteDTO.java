package com.mars.workflow.dto;

import java.time.LocalDate;

public class ClienteDTO {

    private Long id;
    private String nome;
    private String cpf;
    private Double rendaMensal;
    private LocalDate dataCadastro;

    // Construtores
    public ClienteDTO() {}

    public ClienteDTO(Long id, String nome, String cpf, Double rendaMensal, LocalDate dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rendaMensal = rendaMensal;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public Double getRendaMensal() { return rendaMensal; }
    public void setRendaMensal(Double rendaMensal) { this.rendaMensal = rendaMensal; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
}