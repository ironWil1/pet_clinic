package com.vet24.models.annotation;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import java.util.ArrayList;
import java.util.List;

public class ClassImportIntegratorIntegratorProvider
        implements IntegratorProvider {

    ReplicationEventListenerIntegrator replicationEventListenerIntegrator = new ReplicationEventListenerIntegrator();
    List<Integrator> reli = new ArrayList<>();


    @Override
    public List<Integrator> getIntegrators() {
    reli.add(replicationEventListenerIntegrator);
        return reli;
    }
}
