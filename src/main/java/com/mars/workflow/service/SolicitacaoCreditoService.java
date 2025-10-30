package com.mars.workflow.service;

import com.mars.workflow.entity.Cliente;
import com.mars.workflow.entity.SolicitacaoCredito;
import com.mars.workflow.repository.ClienteRepository;
import com.mars.workflow.repository.SolicitacaoCreditoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitacaoCreditoService {

    private final SolicitacaoCreditoRepository solicitacaoRepository;
    private final ClienteRepository clienteRepository;

    public SolicitacaoCreditoService(SolicitacaoCreditoRepository solicitacaoRepository,
                                     ClienteRepository clienteRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<SolicitacaoCredito> listarTodas() {
        return solicitacaoRepository.findAll();
    }

    public List<SolicitacaoCredito> listarPorCliente(Long clienteId) {
        return solicitacaoRepository.findByClienteId(clienteId);
    }

    public Optional<SolicitacaoCredito> buscarPorId(Long id) {
        return solicitacaoRepository.findById(id);
    }

    public SolicitacaoCredito salvar(SolicitacaoCredito solicitacao, Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com ID: " + clienteId));
        solicitacao.setCliente(cliente);
        return solicitacaoRepository.save(solicitacao);
    }

    public void deletar(Long id) {
        solicitacaoRepository.deleteById(id);
    }
}
