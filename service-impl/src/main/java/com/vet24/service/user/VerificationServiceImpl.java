package com.vet24.service.user;

import com.vet24.dao.user.VerificationDao;
import com.vet24.models.user.Client;
import com.vet24.models.user.VerificationToken;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificationServiceImpl extends ReadWriteServiceImpl<Long, VerificationToken>
        implements VerificationService {
    private final VerificationDao verificationDao;

    @Autowired
    protected VerificationServiceImpl(VerificationDao verificationDao) {
        super(verificationDao);
        this.verificationDao = verificationDao;

    }

    @Override
    @Transactional
    public String createVerificationToken(Client client) {
        persistTokenWithClient(client);
        return String.valueOf(client.getId());
    }

    private void persistTokenWithClient(Client client) {
        VerificationToken vt = new VerificationToken(client);
        verificationDao.persist(vt);
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationToken getVerificationToken(String token) {
        return verificationDao.getByKey(Long.parseLong(token));
    }

    @Override
    public VerificationToken findByClientId(long clientId) {
        return verificationDao.findByClientId(clientId);
    }
}
