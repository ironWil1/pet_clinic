package com.vet24.service.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.user.UserDao;
import com.vet24.dao.user.VerificationDao;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import com.vet24.models.user.VerificationToken;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VerificationServiceImpl extends ReadWriteServiceImpl<Long, VerificationToken>
        implements VerificationService{
    private final VerificationDao verificationDao;

    @Autowired
    protected VerificationServiceImpl(ReadWriteDaoImpl<Long, VerificationToken> readWriteDao,
                                      VerificationDao  verificationDao) {
        super(readWriteDao);
        this.verificationDao = verificationDao;

    }

    @Override
    @Transactional
    public  String createVerificationTokenDisplayCode(Client client){
        UUID token = UUID.randomUUID();
        Long tokenId = token.getLeastSignificantBits()^token.getMostSignificantBits();
        VerificationToken vt = new VerificationToken(tokenId,client);
        verificationDao.persist(vt);
        return token.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationToken getVerificationToken(String token) {
        UUID uuid =  UUID.fromString(token);
        Long tokenId = uuid.getLeastSignificantBits()^uuid.getMostSignificantBits();
        return verificationDao.getByKey(tokenId);
    }
}
