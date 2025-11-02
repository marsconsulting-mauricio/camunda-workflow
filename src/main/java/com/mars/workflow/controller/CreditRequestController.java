package com.mars.workflow.controller;

import com.mars.workflow.dto.CreditRequestPayload;
import com.mars.workflow.dto.CreditRequestResponse;
import com.mars.workflow.entity.Cliente;
import com.mars.workflow.service.ClienteService;
import com.mars.workflow.service.EmailService;
import jakarta.validation.Valid;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/credit-requests")
public class CreditRequestController {

    private static final String PROCESS_KEY = "Process_0a3n8my";

    private final ClienteService clienteService;
    private final RuntimeService runtimeService;
    private final EmailService emailService;

    public CreditRequestController(
            ClienteService clienteService,
            RuntimeService runtimeService,
            EmailService emailService
    ) {
        this.clienteService = clienteService;
        this.runtimeService = runtimeService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<CreditRequestResponse> iniciarFluxo(
            @Valid @RequestBody CreditRequestPayload payload
    ) {
        Cliente cliente = new Cliente();
        cliente.setNome(payload.nome());
        cliente.setCpf(payload.cpf());
        cliente.setRendaMensal(payload.rendaMensal());

        Cliente salvo = clienteService.salvarOuAtualizarPorCpf(cliente);

        Map<String, Object> variables = new HashMap<>();
        variables.put("cpf", payload.cpf());
        variables.put("nome", payload.nome());
        variables.put("rendaMensal", payload.rendaMensal());
        variables.put("valorSolicitado", payload.valorSolicitado());
        variables.put("observacoes", payload.observacoes());

        ProcessInstance instance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, variables);

        emailService.sendCreditRequestNotification(payload, salvo.getId(), instance.getProcessInstanceId());

        CreditRequestResponse response = new CreditRequestResponse(
                salvo.getId(),
                instance.getProcessInstanceId(),
                "started"
        );

        return ResponseEntity.ok(response);
    }
}
