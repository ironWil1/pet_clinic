package com.vet24.service.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${spring.mail.location}")
    private String mailLocation;
    @Value("${spring.mail.sign}")
    private String mailSign;

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void sendEmailFromTemplate(String toEmail, String subject, String templateName, Map<String, Object> model) {
        Locale locale = Locale.forLanguageTag("ru-RU");
        Context context = new Context(locale);
        context.setVariables(model);
        String content = templateEngine.process(templateName, context);
        sendMultipartHtmlMessage(toEmail, subject, content);
    }

    @Override
    public void sendMultipartHtmlMessage(String toMail, String subject, String content) {
        var message = emailSender.createMimeMessage();

        try {
            var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(mailFrom, mailSign);
            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setText(content, true);

            var resource = new ClassPathResource("/template-cover-cat-transparent-80.png");
            helper.addInline("logoImage", resource);

            emailSender.send(message);
        } catch (MailException | UnsupportedEncodingException | MessagingException e) {
            e.getStackTrace();  // TODO: 28.06.2021
        }
    }

    @Override
    public void sendWelcomeMessage(String emailTo, String userName, String tokenUrl) {
        var model = new HashMap<String, Object>() {{
            put("tokenUrl", tokenUrl);
            put("name", userName);
            put("sign", mailSign);
            put("location", mailLocation);
        }};
        sendEmailFromTemplate(emailTo, "Registration greeting", "mail/greeting-letter-template", model);
    }

    @Override
    public void sendGeolocationPetFoundMessage(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        emailSender.send(mailMessage);
    }
}
