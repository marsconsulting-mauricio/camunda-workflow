package com.mars.workflow.controller;

import com.mars.workflow.dto.SolicitacaoCreditoDTO;
import com.mars.workflow.entity.SolicitacaoCredito;
import com.mars.workflow.service.SolicitacaoCreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoCreditoController {

    private final SolicitacaoCreditoService solicitacaoService;

    public SolicitacaoCreditoController(SolicitacaoCreditoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoCreditoDTO>> listarTodas() {
        List<SolicitacaoCreditoDTO> lista = solicitacaoService.listarTodas()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SolicitacaoCreditoDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<SolicitacaoCreditoDTO> lista = solicitacaoService.listarPorCliente(clienteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<SolicitacaoCreditoDTO> criar(@RequestBody SolicitacaoCreditoDTO dto) {
        SolicitacaoCredito solicitacao = toEntity(dto);
        SolicitacaoCredito salva = solicitacaoService.salvar(solicitacao, dto.getClienteId());
        return ResponseEntity.ok(toDTO(salva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        solicitacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Conversões
    private SolicitacaoCreditoDTO toDTO(SolicitacaoCredito s) {
        return new SolicitacaoCreditoDTO(
                s.getId(),
                s.getCliente().getId(),
                s.getValorSolicitado(),
                s.getAprovado(),
                s.getLimiteConcedido(),
                s.getParecer(),
                s.getDataSolicitacao()
        );
    }

    private SolicitacaoCredito toEntity(SolicitacaoCreditoDTO dto) {
        SolicitacaoCredito s = new SolicitacaoCredito();
        s.setId(dto.getId());
        s.setValorSolicitado(dto.getValorSolicitado());
        s.setAprovado(dto.getAprovado());
        s.setLimiteConcedido(dto.getLimiteConcedido());
        s.setParecer(dto.getParecer());
        s.setDataSolicitacao(dto.getDataSolicitacao());
        return s;
    }
}
