package com.mars.workflow.camunda.delegate;

import com.mars.workflow.service.EmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.util.Locale;

@Component("sendCreditRequestEmailDelegate")
public class SendCreditRequestEmailDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendCreditRequestEmailDelegate.class);
    private static final String DEFAULT_SUBJECT = "Nova solicitação de crédito registrada";

    private final EmailService emailService;

    public SendCreditRequestEmailDelegate(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String recipient = asString(execution.getVariable("emailRecipient"));
        String subject = asString(execution.getVariable("emailSubject"));
        String body = asString(execution.getVariable("emailBody"));

        if (!StringUtils.hasText(subject)) {
            subject = DEFAULT_SUBJECT;
        }

        if (!StringUtils.hasText(body)) {
            body = buildDefaultBody(execution);
        } else {
            body = body.replace("{processInstanceId}", execution.getProcessInstanceId());
        }

        if (!StringUtils.hasText(body)) {
            LOGGER.warn("Corpo do e-mail não pôde ser montado. Mensagem não será enviada.");
            return;
        }

        System.out.printf("[EmailDelegate] Destinatário=%s | Assunto=%s%n",
                StringUtils.hasText(recipient) ? recipient : "<default>", subject);
        LOGGER.info("[EmailDelegate] Destinatário={}, Assunto='{}'", recipient, subject);
        emailService.send(recipient, subject, body);
    }

    private String asString(Object value) {
        return value != null ? value.toString() : null;
    }

    private Double asDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        try {
            return value != null ? Double.parseDouble(value.toString()) : null;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private Long asLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        try {
            return value != null ? Long.parseLong(value.toString()) : null;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private String buildDefaultBody(DelegateExecution execution) {
        String nome = asString(execution.getVariable("nome"));
        String cpf = asString(execution.getVariable("cpf"));
        Double rendaMensal = asDouble(execution.getVariable("rendaMensal"));
        Double valorSolicitado = asDouble(execution.getVariable("valorSolicitado"));
        String observacoes = asString(execution.getVariable("observacoes"));
        Long clienteId = asLong(execution.getVariable("clienteId"));

        if (!StringUtils.hasText(nome) || !StringUtils.hasText(cpf)) {
            LOGGER.warn("Variáveis 'nome' e 'cpf' são obrigatórias para montar o e-mail.");
            return null;
        }

        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        StringBuilder builder = new StringBuilder();
        builder.append("Uma nova solicitação de crédito foi registrada.\n\n");
        builder.append("Cliente: ").append(nome).append('\n');
        builder.append("CPF: ").append(cpf).append('\n');
        builder.append("Renda mensal: ").append(rendaMensal != null ? currency.format(rendaMensal) : "-").append('\n');
        builder.append("Valor solicitado: ").append(valorSolicitado != null ? currency.format(valorSolicitado) : "-").append('\n');
        if (StringUtils.hasText(observacoes)) {
            builder.append("Observações: ").append(observacoes).append('\n');
        }
        builder.append('\n');
        if (clienteId != null) {
            builder.append("ID do cliente: ").append(clienteId).append('\n');
        }
        builder.append("ID da instância do processo: ").append(execution.getProcessInstanceId()).append('\n');

        return builder.toString();
    }
}
