package com.mars.workflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String from;
    private final String defaultRecipient;

    public EmailService(
            ObjectProvider<JavaMailSender> mailSenderProvider,
            @Value("${spring.mail.username:}") String from,
            @Value("${app.notifications.credit-request.recipient:}") String defaultRecipient
    ) {
        this.mailSender = mailSenderProvider.getIfAvailable();
        this.from = from;
        this.defaultRecipient = StringUtils.hasText(defaultRecipient) ? defaultRecipient : from;
    }

    public void send(String to, String subject, String body) {
        if (mailSender == null || !StringUtils.hasText(from)) {
            LOGGER.warn("Serviço de e-mail não configurado. Mensagem não será enviada.");
            return;
        }

        if (!StringUtils.hasText(subject) || !StringUtils.hasText(body)) {
            LOGGER.warn("Assunto e corpo são obrigatórios para envio de e-mail");
            return;
        }

        String recipient = StringUtils.hasText(to) ? to : defaultRecipient;
        if (!StringUtils.hasText(recipient)) {
            LOGGER.warn("Nenhum destinatário definido para envio de e-mail");
            return;
        }

        try {
            System.out.printf("[EmailService] Preparando envio | from=%s | to=%s | subject=%s%n", from, recipient, subject);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(recipient);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("[EmailService] E-mail enviado com sucesso");
        } catch (Exception exception) {
            LOGGER.error("Falha ao enviar e-mail de notificação", exception);
        }
    }
}
