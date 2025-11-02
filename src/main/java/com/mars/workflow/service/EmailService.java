package com.mars.workflow.service;

import com.mars.workflow.dto.CreditRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String from;
    private final String defaultRecipient;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String from,
            @Value("${app.notifications.credit-request.recipient:${spring.mail.username}}") String defaultRecipient
    ) {
        this.mailSender = mailSender;
        this.from = from;
        this.defaultRecipient = defaultRecipient;
    }

    public void sendCreditRequestNotification(CreditRequestPayload payload, Long clienteId, String processInstanceId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(defaultRecipient);
            message.setSubject("Nova solicitação de crédito recebida");
            message.setText(buildBody(payload, clienteId, processInstanceId));

            mailSender.send(message);
        } catch (Exception exception) {
            LOGGER.error("Falha ao enviar e-mail de notificação", exception);
        }
    }

    private String buildBody(CreditRequestPayload payload, Long clienteId, String processInstanceId) {
        StringBuilder body = new StringBuilder();
        body.append("Uma nova solicitação de crédito foi registrada.\n\n");
        body.append("Cliente: ").append(payload.nome()).append('\n');
        body.append("CPF: ").append(payload.cpf()).append('\n');
        body.append("Renda mensal: ").append(formatCurrency(payload.rendaMensal())).append('\n');
        body.append("Valor solicitado: ").append(formatCurrency(payload.valorSolicitado())).append('\n');
        if (payload.observacoes() != null && !payload.observacoes().isBlank()) {
            body.append("Observações: ").append(payload.observacoes()).append("\n");
        }
        body.append("\n");
        body.append("ID do cliente: ").append(clienteId).append('\n');
        body.append("ID da instância do processo: ").append(processInstanceId).append('\n');
        return body.toString();
    }

    private String formatCurrency(Double value) {
        if (value == null) {
            return "-";
        }
        return String.format(java.util.Locale.forLanguageTag("pt-BR"), "R$ %,.2f", value);
    }
}
