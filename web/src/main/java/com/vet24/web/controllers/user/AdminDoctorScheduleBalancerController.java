package com.vet24.web.controllers.user;

import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.web.util.DoctorScheduleBalanceUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "api/admin/doctor/schedule")
@Tag(name = "admin doctor schedule balance controller", description = "adminDoctorScheduleBalanceController operations")
@Slf4j
@Transactional
public class AdminDoctorScheduleBalancerController {

    private final DoctorScheduleBalanceUtil doctorScheduleBalanceUtil;
    private final DoctorScheduleService doctorScheduleService;
    private List<DoctorSchedule> resultDoctorSchedule;

    @Autowired
    public AdminDoctorScheduleBalancerController(DoctorScheduleService doctorScheduleService,
                                                 DoctorScheduleBalanceUtil doctorScheduleBalanceUtil) {
        this.doctorScheduleService = doctorScheduleService;
        this.doctorScheduleBalanceUtil = doctorScheduleBalanceUtil;
    }

    @Operation(summary = "Balancer doctor schedule")
    @ApiResponse(responseCode = "200", description = "The load balancer has successfully worked")
    @Scheduled(cron = "0 0 3 * * *")
    @GetMapping("/balance")
    public ResponseEntity<Void> balanceDoctorSchedule() {
        resultDoctorSchedule = doctorScheduleBalanceUtil.getDoctorScheduleList();

//        Collections.sort(resultDoctorSchedule, (i1, i2) -> (int)(i1.getDoctor().getId() - i2.getDoctor().getId()));
//
//        for (DoctorSchedule ds : resultDoctorSchedule) {
//            log.info(ds.getDoctor().getId() + " " + ds.getDoctor().getEmail() + " " + ds.getWeekNumber() + " " + ds.getWorkShift().toString());
//        }
        if (!resultDoctorSchedule.isEmpty()) {
            doctorScheduleService.persistAll(resultDoctorSchedule);
        }

        return ResponseEntity.ok().build();
    }
}
