package com.vet24.web.controllers.user;

import com.vet24.models.enums.WorkShift;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.user.Doctor;
import com.vet24.service.medicine.DoctorScheduleService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/admin/doctor/schedule")
@Tag(name = "admin doctor schedule balance controller", description = "adminDoctorScheduleBalanceController operations")
@Slf4j
@Transactional
public class AdminDoctorScheduleBalancerController {

    private final DoctorService doctorService;
    private final DoctorScheduleBalanceUtil doctorScheduleBalanceUtil;
    private final DoctorScheduleService doctorScheduleService;
    //Выходной лист смен докторов для записи в БД
    private List<DoctorSchedule> resultDoctorSchedule = new ArrayList<>();
    private int localFirstShiftCount;
    private int localSecondShiftCount;

    @Autowired
    public AdminDoctorScheduleBalancerController(DoctorService doctorService,
                                                 DoctorScheduleService doctorScheduleService,
                                                 DoctorScheduleBalanceUtil doctorScheduleBalanceUtil) {
        this.doctorService = doctorService;
        this.doctorScheduleService = doctorScheduleService;
        this.doctorScheduleBalanceUtil = doctorScheduleBalanceUtil;
    }

    @Operation(summary = "Balancer doctor schedule")
    @ApiResponse(responseCode = "200", description = "The load balancer has successfully worked")
    @GetMapping("/balance")
    @Scheduled(fixedDelay = 50000)
    public ResponseEntity<Void> balanceDoctorSchedule() {
        //Получение всех докторов
        List<Doctor> doctorList = doctorService.getAll();

        //Начало итераций цикла по неделям текущего месяца
        for (int currentWeek = doctorScheduleBalanceUtil.getStartWeek(); currentWeek <= doctorScheduleBalanceUtil.getEndWeek(); currentWeek++) {
            //for (int currentWeek = doctorScheduleBalanceUtil.getStartWeek() - 1; currentWeek < doctorScheduleBalanceUtil.getStartWeek(); currentWeek++) {
            localFirstShiftCount = 0;
            localSecondShiftCount = 0;
            //
            doctorScheduleBalanceUtil.calculateFirstSecondShiftForWeek(currentWeek);

            //Загружаем докторов работающих в текущей неделе, за минусом неработающих
            int finalCurrentWeek = currentWeek;
            List<Doctor> doctorCurrentWeek = doctorList.stream()
                    .filter(doc -> !doctorScheduleBalanceUtil.doctorNonWorkingByIdAndWeek(doc.getId(), finalCurrentWeek))
                    .collect(Collectors.toList());

            //Проверка если первое число месяца, заполняем базу полностью на месяц, исключая неработающих
            if (doctorScheduleBalanceUtil.getLocalDate().getDayOfMonth() == 1) {
                for (int i = 0; i < doctorCurrentWeek.size(); i++) {
                    if (doctorCurrentWeek.get(i).getId() == 71) continue;

                    if (doctorScheduleBalanceUtil.getShiftByWeek(doctorCurrentWeek.get(i).getId(), currentWeek - 1).equals("SECOND_SHIFT")) {
                        resultDoctorSchedule.add(new DoctorSchedule(doctorCurrentWeek.get(i), WorkShift.FIRST_SHIFT, currentWeek));
                        localFirstShiftCount++;
                    } else if (doctorScheduleBalanceUtil.getShiftByWeek(doctorCurrentWeek.get(i).getId(), currentWeek - 1).equals("FIRST_SHIFT")) {
                        resultDoctorSchedule.add(new DoctorSchedule(doctorCurrentWeek.get(i), WorkShift.SECOND_SHIFT, currentWeek));
                        localSecondShiftCount++;
                    } else {
                        if (localFirstShiftCount < localSecondShiftCount) {
                            resultDoctorSchedule.add(new DoctorSchedule(doctorCurrentWeek.get(i), WorkShift.FIRST_SHIFT, currentWeek));
                            localFirstShiftCount++;
                        } else {
                            resultDoctorSchedule.add(new DoctorSchedule(doctorCurrentWeek.get(i), WorkShift.SECOND_SHIFT, currentWeek));
                            localSecondShiftCount++;
                        }
                    }
                }

                //Если не первое число, значит база у нас заполнена, проверяем на вновь прибывших докторов, в зависимости
                //от того где докторов меньше в смене, добавляем ему эту смену - РАБОТАЕТ
            } else {
                for (int i = 0; i < doctorCurrentWeek.size(); i++) {
                    if (doctorScheduleBalanceUtil.doctorScheduleExistsByIdAndWeek(doctorCurrentWeek.get(i).getId(), currentWeek) == false) {
                        System.out.println("В текущей неделе " + currentWeek + " работающих в FIRST_SHIFT " + doctorScheduleBalanceUtil.getCountWorkWeekFirstShift());
                        System.out.println("В текущей неделе " + currentWeek + " работающих в SECOND_SHIFT " + doctorScheduleBalanceUtil.getCountWorkWeekSecondShift());
                        if (doctorScheduleBalanceUtil.getCountWorkWeekFirstShift() < doctorScheduleBalanceUtil.getCountWorkWeekSecondShift()) {
                            resultDoctorSchedule.add(new DoctorSchedule(doctorCurrentWeek.get(i), WorkShift.FIRST_SHIFT, currentWeek));
                            doctorScheduleBalanceUtil.setCountWorkWeekFirstShift(doctorScheduleBalanceUtil.getCountWorkWeekFirstShift() + 1);
                        } else {
                            resultDoctorSchedule.add(new DoctorSchedule(doctorCurrentWeek.get(i), WorkShift.SECOND_SHIFT, currentWeek));
                            doctorScheduleBalanceUtil.setCountWorkWeekSecondShift(doctorScheduleBalanceUtil.getCountWorkWeekSecondShift() + 1);
                        }
                    }
                }
                //Удалить
                System.out.println("Стало текущей неделе " + currentWeek + " работающих в FIRST_SHIFT " + doctorScheduleBalanceUtil.getCountWorkWeekFirstShift());
                System.out.println("Стало текущей неделе " + currentWeek + " работающих в SECOND_SHIFT " + doctorScheduleBalanceUtil.getCountWorkWeekSecondShift());
            }

            System.out.println("/nНеделя " + currentWeek);
            System.out.println("Локальный счетчик FIRST - " + localFirstShiftCount);
            System.out.println("Локальный счетчик SECOND - " + localSecondShiftCount + "/n");

            doctorScheduleService.persistAll(resultDoctorSchedule);
            doctorScheduleBalanceUtil.loadingDoctorScheduleFromDBToMap();
            resultDoctorSchedule.clear();
        }

//        for (int i = 0; i < resultDoctorSchedule.size(); i++) {
//            DoctorSchedule doctorSchedule = resultDoctorSchedule.get(i);
//            System.out.println(doctorSchedule.getDoctor().getId() + " " + doctorSchedule.getDoctor().getFirstname() + " " + doctorSchedule.getWorkShift() + " " + doctorSchedule.getWeekNumber());
//        }

        log.info("BreakPoint");
        return ResponseEntity.ok().build();
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