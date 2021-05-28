package com.vet24.service.medicine;

import com.vet24.dao.medicine.TreatmentDao;
import com.vet24.models.medicine.Treatment;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentServiceImpl extends ReadWriteServiceImpl<Long, Treatment>  implements TreatmentService {

    private final TreatmentDao cureProcessDao;

    @Autowired
    public TreatmentServiceImpl(TreatmentDao treatmentDao) {
        super(treatmentDao);
        this.cureProcessDao = treatmentDao;
    }
}
