package com.vet24.web.controllers.user;

import com.vet24.web.util.DoctorScheduleBalanceUtil;
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
@RequestMapping(value = "api/admin/doctor/schedule")
@Tag(name = "admin doctor schedule balance controller", description = "adminDoctorScheduleBalanceController operations")
@Slf4j
public class AdminDoctorScheduleBalancerController {

    private final DoctorScheduleBalanceUtil doctorScheduleBalanceUtil;

    @Autowired
    public AdminDoctorScheduleBalancerController(DoctorScheduleBalanceUtil doctorScheduleBalanceUtil) {
        this.doctorScheduleBalanceUtil = doctorScheduleBalanceUtil;
    }

    @Operation(summary = "Balancer doctor schedule")
    @ApiResponse(responseCode = "200", description = "The load balancer has successfully worked")

    @GetMapping("/balance")
    public ResponseEntity<Void> balanceDoctorSchedule() {
        doctorScheduleBalanceUtil.getBalancer();

        return ResponseEntity.ok().build();
    }
}
