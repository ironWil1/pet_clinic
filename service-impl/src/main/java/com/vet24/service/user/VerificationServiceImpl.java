package com.vet24.service.user;

import com.eatthepath.uuid.FastUUID;
import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.user.VerificationDao;
import com.vet24.models.user.Client;
import com.vet24.models.user.VerificationToken;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class VerificationServiceImpl extends ReadWriteServiceImpl<Long, VerificationToken>
        implements VerificationService{

    @Autowired
    protected VerificationServiceImpl(ReadWriteDaoImpl<Long, VerificationToken> readWriteDao,
                                      VerificationDao  verificationDao) {
        super(readWriteDao);
    }

    @Override
    @Transactional
    public  String createVerificationTokenDisplayCode(Client client){
        UUID randomUUID =  UUID.randomUUID();
        Long tokenId = randomUUID.getLeastSignificantBits()*37+11;
        VerificationToken vt = new VerificationToken(tokenId,client);
        super.persist(vt);
        return FastUUID.toString(randomUUID);
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationToken getVerificationToken(String token) {
        Long tokenId = FastUUID.parseUUID(token).getLeastSignificantBits()*37+11;
        return super.getByKey(tokenId);
    }
}
