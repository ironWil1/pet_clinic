package com.vet24.models.annotation;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import java.util.ArrayList;
import java.util.List;

public class IntegratorProviderImpl implements IntegratorProvider {

    EventListenerIntegrator replicationEventListenerIntegrator = new EventListenerIntegrator();
    List<Integrator> setFields = new ArrayList<>();


    @Override
    public List<Integrator> getIntegrators() {
        setFields.add(replicationEventListenerIntegrator);
        return setFields;
    }
}
