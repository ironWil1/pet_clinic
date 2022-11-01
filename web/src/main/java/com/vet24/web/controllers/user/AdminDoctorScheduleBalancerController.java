package com.vet24.web.controllers.user;

import com.vet24.service.medicine.DoctorScheduleBalancerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admin/cron")
@Tag(name = "admin doctor schedule balance controller", description = "adminDoctorScheduleBalanceController operations")
@Slf4j
public class AdminDoctorScheduleBalancerController {

    private final DoctorScheduleBalancerImpl doctorScheduleBalancer;

    @Autowired
    public AdminDoctorScheduleBalancerController(DoctorScheduleBalancerImpl doctorScheduleBalancer) {
        this.doctorScheduleBalancer = doctorScheduleBalancer;
    }

    @Operation(summary = "Balance doctor schedule")
    @ApiResponse(responseCode = "200", description = "The load balance has successfully worked")

    @GetMapping("/schedule")
    public ResponseEntity<Void> balanceDoctorSchedule() {
        doctorScheduleBalancer.balance();

        return ResponseEntity.ok().build();
    }
}
