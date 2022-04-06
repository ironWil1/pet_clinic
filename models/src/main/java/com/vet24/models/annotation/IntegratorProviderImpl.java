package com.vet24.models.annotation;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import java.util.ArrayList;
import java.util.List;

public class IntegratorProviderImpl implements IntegratorProvider {

    ReplicationEventListenerIntegrator replicationEventListenerIntegrator = new ReplicationEventListenerIntegrator();
    List<Integrator> setFields = new ArrayList<>();


    @Override
    public List<Integrator> getIntegrators() {
        setFields.add(replicationEventListenerIntegrator);
        return setFields;
    }
}
