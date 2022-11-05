package com.vet24.service;

import com.vet24.service.medicine.DoctorScheduleBalancerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppTaskScheduler {

    private final DoctorScheduleBalancerImpl doctorScheduleBalancer;


    @Autowired
    public AppTaskScheduler(DoctorScheduleBalancerImpl doctorScheduleBalancer) {
        this.doctorScheduleBalancer = doctorScheduleBalancer;
    }

    @Scheduled(cron = "${cron.expression}")
    public void scheduleBalance() {

        log.info("Balance started.");

        doctorScheduleBalancer.balance();

        log.info("Balance finished.");
    }


}
