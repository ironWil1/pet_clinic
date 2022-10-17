package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.Dosage;
import org.springframework.stereotype.Repository;

@Repository
public class DosageDaoImpl extends ReadWriteDaoImpl<Long, Dosage> implements DosageDao {
}
