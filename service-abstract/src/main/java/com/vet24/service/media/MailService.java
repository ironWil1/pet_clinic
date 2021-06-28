package com.vet24.service.media;

import java.util.Map;

public interface MailService {

    void sendWelcomeMessage (String email, String name, String tokenUrl);

    void sendGeolocationPetFoundMessage(String emailTo, String subject, String message);

    void sendEmailFromTemplate(String toEmail, String subject, String templateName, Map<String, Object> model);
    void sendMultipartHtmlMessage(String toMail, String subject, String content);
}
