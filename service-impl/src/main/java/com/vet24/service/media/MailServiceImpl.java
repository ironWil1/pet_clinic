package com.vet24.service.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
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
    @Autowired
    private ServerProperties server;

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
            //helper.setText("<h1>sdfsdfsd</h1> <p>sdfsdfs <b>a00dfsdfsdfdsf</b></p><hr /><img src='cid:logoImage' />", true);
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
//        new EnvironmentUtil();
        var model = new HashMap<String, Object>() {{
            //ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            put("tokenUrl", tokenUrl);
            put("name", userName);
            put("sign", mailSign);
            put("location", mailLocation);
        }};
        sendEmailFromTemplate(emailTo, "Registration greeting", "mail/greeting-letter-template", model);

        /*MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.addAttachment("template-cover-cat.png",
                new ClassPathResource("template-cover-cat.png"));
        Context context = new Context(Locale.ENGLISH);
        Map<String, Object> model = new HashMap<>();
        model.put("name",name );
        model.put("location", mailLocation);
        model.put("sign" ,mailSign);
        model.put("tokenLink", tokenLink);
        context.setVariables(model);
        String html = templateEngine.process("greeting-letter-template", context);
        helper.setTo(emailTo);
        helper.setText(html, true);
        helper.setSubject("Registration greeting");
        helper.setFrom(mailFrom);
        emailSender.send(message);
        //send(message);*/
    }

    @Override
    public void sendGeolocationPetFoundMessage(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        emailSender.send(mailMessage);
        //send(mailMessage);
    }
}
