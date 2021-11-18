package com.vet24.service.media;

import com.vet24.models.pet.PetContact;

import java.util.Map;

public interface MailService {

    void sendWelcomeMessage (String email, String name, String tokenUrl);

    void sendGeolocationPetFoundMessage(PetContact petContact, String geolocationPetFoundUrl, String text);

    void sendEmailFromTemplate(String toEmail, String subject, String templateName, Map<String, Object> model);

    void sendMultipartHtmlMessage(String toMail, String subject, String content);
}
