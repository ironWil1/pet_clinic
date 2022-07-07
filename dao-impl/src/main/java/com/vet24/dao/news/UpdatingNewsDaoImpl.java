package com.vet24.dao.news;

import com.vet24.dao.ReadWriteDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class UpdatingNewsDaoImpl extends ReadWriteDaoImpl<Long, UpdatingNews> implements UpdatingNewsDao {
}
