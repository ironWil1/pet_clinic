package com.vet24.models.annotation;

import org.hibernate.integrator.spi.Integrator;

import java.util.ArrayList;
import java.util.List;

public class IntegratorProviderImpl
        implements org.hibernate.jpa.boot.spi.IntegratorProvider {

    EventListenerIntegrator eventListenerIntegrator = new EventListenerIntegrator();
    List<Integrator> reli = new ArrayList<>();


    @Override
    public List<Integrator> getIntegrators() {
    reli.add(eventListenerIntegrator);
        return reli;
    }
}
