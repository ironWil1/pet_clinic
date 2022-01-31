package com.vet24.web.controllers.user;

import com.vet24.models.enums.WorkShift;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.user.Doctor;
import com.vet24.service.user.DoctorService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/admin/doctor/schedule")
@Tag(name = "admin doctor schedule balance controller", description = "adminDoctorScheduleBalanceController operations")
@Slf4j
@Transactional
public class AdminDoctorScheduleBalancerController {

    private final DoctorService doctorService;
    private final DoctorScheduleBalanceUtil doctorScheduleBalanceUtil;
    //Выходной лист смен докторов для записи в БД
    private List<DoctorSchedule> resultDoctorSchedule = new ArrayList<>();

    @Autowired
    public AdminDoctorScheduleBalancerController(DoctorService doctorService,
                                                 DoctorScheduleBalanceUtil doctorScheduleBalanceUtil) {
        this.doctorService = doctorService;
        this.doctorScheduleBalanceUtil = doctorScheduleBalanceUtil;
    }

    @Operation(summary = "Balancer doctor schedule")
    @ApiResponse(responseCode = "200", description = "The load balancer has successfully worked")
    @GetMapping("/balance")
    @Scheduled(fixedDelay = 50000)
    public ResponseEntity<Void> balanceDoctorSchedule() {
        //Получение всех докторов
        List<Doctor> doctorList = doctorService.getAll();

        for (int currentWeek = doctorScheduleBalanceUtil.getStartWeek(); currentWeek <= doctorScheduleBalanceUtil.getEndWeek(); currentWeek++) {
            for (Doctor doctor : doctorList) {
                //if (LocalDate.now().getDayOfMonth() == 1) {
                if (LocalDate.of(2022, 1, 1).getDayOfMonth() == 1) {
                    //проверка доктора, не болен ли он на этой неделе, в отпуске или отгуле
                    if (doctorScheduleBalanceUtil.doctorNonWorkingByIdAndWeek(doctor.getId(), currentWeek)) {
                        continue;
                    }
                    balancer(currentWeek, doctor);
                } else {
                    //проверка для доктора, если он новый, то добавляем смены только ему
                    if (doctorScheduleBalanceUtil.doctorScheduleExistsByIdAndWeek(doctor.getId(), currentWeek)
                            || doctorScheduleBalanceUtil.doctorNonWorkingByIdAndWeek(doctor.getId(), currentWeek)) {
                        continue;
                    }
                    balancer(currentWeek, doctor);
                }
            }
        }

        for (int i = 0; i < resultDoctorSchedule.size(); i++) {
            DoctorSchedule doctorSchedule = resultDoctorSchedule.get(i);
            System.out.println(doctorSchedule.getDoctor().getId() + " " + doctorSchedule.getDoctor().getFirstname() + " " + doctorSchedule.getWorkShift() + " " + doctorSchedule.getWeekNumber());
        }

        //doctorScheduleService.updateAll(resultDoctorSchedule);
        log.info("BreakPoint");
        return ResponseEntity.ok().build();
    }

    private void balancer(int week, Doctor doctor) {
        if (week % 2 == 0) {
            if (doctor.getId() % 2 == 0) {
                resultDoctorSchedule.add(new DoctorSchedule(doctor, WorkShift.FIRST_SHIFT, week));
            } else {
                resultDoctorSchedule.add(new DoctorSchedule(doctor, WorkShift.SECOND_SHIFT, week));
            }
        } else {
            if (doctor.getId() % 2 != 0) {
                resultDoctorSchedule.add(new DoctorSchedule(doctor, WorkShift.FIRST_SHIFT, week));
            } else {
                resultDoctorSchedule.add(new DoctorSchedule(doctor, WorkShift.SECOND_SHIFT, week));
            }
        }
    }
}
/*
GET /api/admin/doctor/schedule/balance - создать контроллер который будет распределять смены на докторов на ближайший
месяц (следующий за текущим). При чем:

1. этот контроллер должен отрабатывать раз в день по расписанию, либо его можно будет самому вызывать
2. на каждую неделю должно быть примерно равное кол-во докторов в смену
3. у доктора не должна быть одна и та же смена повторяться больше 2 раз подряд и за прошедший месяц
   (т.е. если отмотать 4 недели назад от любой даты то там не должно быть больше 2 одинаковых смен)
4. при формировании расписания учитывать и данные о нерабочих днях у докторов - на потом
 */