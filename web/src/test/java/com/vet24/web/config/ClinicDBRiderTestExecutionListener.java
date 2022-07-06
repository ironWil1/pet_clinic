package com.vet24.web.config;

import com.github.database.rider.spring.DBRiderTestExecutionListener;
import org.springframework.core.Ordered;

import java.util.TimeZone;

public class ClinicDBRiderTestExecutionListener extends DBRiderTestExecutionListener {

    @Override
    public int getOrder() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
