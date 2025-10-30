package com.mars.workflow.camunda.delegate;

import com.mars.workflow.entity.Cliente;
import com.mars.workflow.entity.SolicitacaoCredito;
import com.mars.workflow.repository.ClienteRepository;
import com.mars.workflow.repository.SolicitacaoCreditoRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Component("registrarSolicitacaoCreditoDelegate")
public class RegistrarSolicitacaoCreditoDelegate implements JavaDelegate {

    private final ClienteRepository clienteRepository;
    private final SolicitacaoCreditoRepository solicitacaoRepository;

    public RegistrarSolicitacaoCreditoDelegate(ClienteRepository clienteRepository,
                                               SolicitacaoCreditoRepository solicitacaoRepository) {
        this.clienteRepository = clienteRepository;
        this.solicitacaoRepository = solicitacaoRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        // Variáveis básicas do processo
        String cpf = (String) execution.getVariable("cpf");
        Double valorSolicitado = (Double) execution.getVariable("valorSolicitado");

        // Recupera o resultado da DMN (que é um HashMap)
        Map<String, Object> resultado = (Map<String, Object>) execution.getVariable("resultadoAnalise");

        if (resultado == null) {
            throw new RuntimeException("Resultado da análise de crédito não encontrado na variável 'resultadoAnalise'.");
        }

        // Extrai valores do mapa retornado pela DMN
        String status = (String) resultado.get("resultadoAnalise");  // ex: "APROVADO", "MANUAL", "REPROVADO"
        Double limiteConcedido = (Double) resultado.get("limiteConcedido");

        // Define valores derivados
        Boolean aprovado = "APROVADO".equalsIgnoreCase(status);
        String parecer = status; // pode ajustar se quiser textos diferentes para o relatório

        // Busca o cliente no banco
        Optional<Cliente> optCliente = clienteRepository.findByCpf(cpf);
        if (optCliente.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado com CPF: " + cpf);
        }

        Cliente cliente = optCliente.get();

        // Monta e salva a solicitação
        SolicitacaoCredito solicitacao = new SolicitacaoCredito();
        solicitacao.setCliente(cliente);
        solicitacao.setValorSolicitado(valorSolicitado);
        solicitacao.setAprovado(aprovado);
        solicitacao.setLimiteConcedido(limiteConcedido);
        solicitacao.setParecer(parecer);
        solicitacao.setDataSolicitacao(LocalDate.now());

        solicitacaoRepository.save(solicitacao);

        // (Opcional) também grava variáveis no contexto do processo
        execution.setVariable("aprovado", aprovado);
        execution.setVariable("limiteConcedido", limiteConcedido);
        execution.setVariable("parecer", parecer);

        System.out.printf(
                "[RegistrarSolicitacaoCreditoDelegate] CPF=%s | Valor=%.2f | Status=%s | Limite=%.2f%n",
                cpf, valorSolicitado, status, limiteConcedido
        );
    }
}
