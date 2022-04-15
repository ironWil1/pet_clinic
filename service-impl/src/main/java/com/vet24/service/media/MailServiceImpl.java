package com.vet24.service.media;

import com.vet24.models.dto.notification.MailNotification;
import com.vet24.models.pet.PetContact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Service
@Slf4j
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
        model.put("sign", mailSign);
        model.put("location", mailLocation);
        Context context = new Context(locale);
        context.setVariables(model);
        String content = templateEngine.process(templateName, context);
        sendMultipartHtmlMessage(toEmail, subject, content);
    }

    @Override
    public void sendMultipartHtmlMessage(String toMail, String subject, String content) {
        var message = emailSender.createMimeMessage();
        log.info("Message {} is created", message);
        try {
            var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(mailFrom, mailSign);
            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setText(content, true);

            var resource = new ClassPathResource("/template-cover-cat-transparent-80.png");
            helper.addInline("logoImage", resource);

            emailSender.send(message);
            log.info("Message has been sent");
        } catch (MailException | UnsupportedEncodingException | MessagingException e) {
            log.warn("{}", e.getMessage());
            e.getStackTrace();
        }
    }

    // TODO: необходимо переделать метод получения email из NotificationDTO
    @Override
    public void sendNotificationMassage(List<MailNotification> mailNotifications) {
        var message = emailSender.createMimeMessage();
        log.info("Message {} is created", message);
        try {
            for (MailNotification n : mailNotifications) {
                var helper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.name());
                helper.setFrom(mailFrom, mailSign);
                helper.setTo(n.getEmail());
                helper.setText(n.getContent());

                var resource = new ClassPathResource("/template-cover-cat-transparent-80.png");
                helper.addInline("logoImage", resource);

                emailSender.send(message);
                log.info("Message has been sent");
            }
        } catch (MailException | UnsupportedEncodingException | MessagingException e) {
            log.warn("{}", e.getMessage());
            e.getStackTrace();
        }
    }

    @Override
    public void sendWelcomeMessage(String emailTo, String userName, String tokenUrl) {
        var model = new HashMap<String, Object>();
        model.put("tokenUrl", tokenUrl);
        model.put("name", userName);
        sendEmailFromTemplate(emailTo, "Registration greeting", "mail/greeting-letter-template", model);
    }

    @Override
    public void sendGeolocationPetFoundMessage(PetContact petContact, String geolocationPetFoundUrl, String text) {
        var model = new HashMap<String, Object>();
        model.put("name", petContact.getPet().getClient().getFirstname());
        model.put("geolocationPetFoundUrl", geolocationPetFoundUrl);
        model.put("petName", petContact.getPet().getName());
        model.put("text", text);
        sendEmailFromTemplate(petContact.getPet().getClient().getEmail(), "Info about your founded pet",
                "mail/geolocation-pet-letter-template", model);
    }
}
