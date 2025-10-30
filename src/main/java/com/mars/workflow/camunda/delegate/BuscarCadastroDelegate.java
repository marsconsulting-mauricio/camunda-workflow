package com.mars.workflow.delegate;

import com.mars.workflow.entity.Cliente;
import com.mars.workflow.repository.ClienteRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("buscarCadastroDelegate")
public class BuscarCadastroDelegate implements JavaDelegate {

    private final ClienteRepository clienteRepository;

    public BuscarCadastroDelegate(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Obtém CPF do contexto do processo
        String cpf = (String) execution.getVariable("cpf");

        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não informado na variável do processo.");
        }

        System.out.println("[BuscarCadastroDelegate] Iniciando busca pelo CPF: " + cpf);

        // Busca no banco
        Optional<Cliente> clienteOpt = clienteRepository.findByCpf(cpf);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            // Define variáveis de retorno
            execution.setVariable("clienteExiste", true);
            execution.setVariable("clienteId", cliente.getId());
            execution.setVariable("nome", cliente.getNome());
            execution.setVariable("rendaMensal", cliente.getRendaMensal());

            System.out.printf("[BuscarCadastroDelegate] Cliente encontrado: ID=%d, Nome=%s%n",
                    cliente.getId(), cliente.getNome());
        } else {
            // Marca que não existe para o gateway decidir pelo cadastro
            execution.setVariable("clienteExiste", false);

            System.out.println("[BuscarCadastroDelegate] Cliente não encontrado. Seguirá para cadastro.");
        }
    }
}
