package com.vet24.service.user;

import com.vet24.models.user.User;
import com.vet24.models.user.VerificationToken;
import com.vet24.service.ReadWriteService;

public interface VerificationService extends ReadWriteService<Long, VerificationToken> {

    String createVerificationToken(User client);

    VerificationToken getVerificationToken(String token);

    VerificationToken findByClientId(long clientId);

}
