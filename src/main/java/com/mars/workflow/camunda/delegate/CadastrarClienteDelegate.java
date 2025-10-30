package com.mars.workflow.delegate;

import com.mars.workflow.entity.Cliente;
import com.mars.workflow.repository.ClienteRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("cadastrarClienteDelegate")
public class CadastrarClienteDelegate implements JavaDelegate {

    private final ClienteRepository clienteRepository;

    public CadastrarClienteDelegate(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Recupera variáveis do processo
        String nome = (String) execution.getVariable("nome");
        String cpf = (String) execution.getVariable("cpf");
        Double rendaMensal = (Double) execution.getVariable("rendaMensal");

        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF é obrigatório para o cadastro de cliente.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório para o cadastro de cliente.");
        }

        if (rendaMensal == null) {
            rendaMensal = 0.0;
        }

        // Cria a entidade e persiste no banco
        Cliente novoCliente = new Cliente();
        novoCliente.setNome(nome);
        novoCliente.setCpf(cpf);
        novoCliente.setRendaMensal(rendaMensal);

        Cliente salvo = clienteRepository.save(novoCliente);

        // Define variáveis de retorno no processo
        execution.setVariable("clienteId", salvo.getId());
        execution.setVariable("clienteEncontrado", true);

        System.out.println("Novo cliente cadastrado com sucesso: " + salvo.getNome() + " (ID: " + salvo.getId() + ")");
    }
}
