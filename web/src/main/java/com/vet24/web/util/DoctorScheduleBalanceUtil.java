package com.vet24.web.util;

import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.service.user.DoctorNonWorkingService;
import com.vet24.service.user.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@EnableScheduling
public class DoctorScheduleBalanceUtil {
    private final DoctorScheduleService doctorScheduleService;
    private final DoctorNonWorkingService doctorNonWorkingService;
    private final DoctorService doctorService;

    //Map(Id доктора, List с неделями)
    private Map<Long, List<Integer>> doctorNonWorkingMap = new HashMap<>();
    //Map <ID доктора, List<Неделя>>
    private Map<Long, List<Integer>> doctorScheduleMap = new HashMap<>();
    private int startWeek;
    private int endWeek;

    @Autowired
    public DoctorScheduleBalanceUtil(DoctorScheduleService doctorScheduleService, DoctorNonWorkingService doctorNonWorkingService, DoctorService doctorService) {
        this.doctorScheduleService = doctorScheduleService;
        this.doctorNonWorkingService = doctorNonWorkingService;
        this.doctorService = doctorService;
    }

    public boolean doctorNonWorkingByIdAndWeek(Long doctorId, int week) {
//        if (doctorNonWorkingMap == null) {
//            initialize();
//        }
        if (doctorNonWorkingMap.containsKey(doctorId) && doctorNonWorkingMap.get(doctorId).contains(week)) {
            return true;
        }
        return false;
    }

    public boolean doctorScheduleExistsByIdAndWeek(Long doctorId, int week) {
//        if (doctorScheduleMap == null) {
//            initialize();
//        }
        if (doctorScheduleMap.containsKey(doctorId) && doctorScheduleMap.get(doctorId).contains(week)) {
            return true;
        }
        return false;
    }

    private void initialize() {
        //LocalDate localDate = LocalDate.now();
        LocalDate localDate = LocalDate.of(2022, 1, 1);
        LocalDate startDate = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        LocalDate endDate = LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.lengthOfMonth());

        int currentDayOfWeek = startDate.getDayOfWeek().getValue();
        System.out.println("День недели - " + currentDayOfWeek);

        if (currentDayOfWeek != 1) {
            startWeek = startDate.plusDays(8 - currentDayOfWeek).get(WeekFields.of(Locale.getDefault()).weekOfYear());
            System.out.println("Текущая неделя - " + startWeek);
        } else {
            startWeek = startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        }

        endWeek = endDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        System.out.println("Последняя неделя месяца - " + endWeek);
        int endDayofWeek = endDate.getDayOfWeek().getValue();
        System.out.println("Последний день месяца, день недели " + endDayofWeek);

        //Получаем всех неработающих докторов в будущем месяце, переводим в Map(Id доктора, List с неделями)
        doctorNonWorkingService.getAll()
                .stream()
                .filter(week -> week.getDate().isAfter(startDate.minusDays(1)))
                .forEach(doc -> {
                    if (doctorNonWorkingMap.get(doc.getDoctor().getId()) == null) {
                        List<Integer> tmpList = new ArrayList<>();
                        tmpList.add(doc.getDate().get(WeekFields.of(Locale.getDefault()).weekOfYear()));
                        doctorNonWorkingMap.put(doc.getDoctor().getId(), tmpList);
                    } else {
                        List<Integer> tmpList = doctorNonWorkingMap.get(doc.getDoctor().getId());
                        tmpList.add(doc.getDate().get(WeekFields.of(Locale.getDefault()).weekOfYear()));
                        doctorNonWorkingMap.put(doc.getDoctor().getId(), tmpList);
                    }
                });

        //Удалить после тестов
        System.out.println(doctorNonWorkingMap);

        //Получение все смен докторов за месяц
        //Создаем Map <ID доктора, List<Неделя>>
        doctorScheduleService.getAll()
                .stream()
                .filter(week -> week.getWeekNumber() >= startWeek)
                .forEach(doc -> {
                    if (doctorScheduleMap.get(doc.getDoctor().getId()) == null) {
                        List<Integer> tmpList = new ArrayList<>();
                        tmpList.add(doc.getWeekNumber());
                        doctorScheduleMap.put(doc.getDoctor().getId(), tmpList);
                    } else {
                        List<Integer> tmpList = doctorScheduleMap.get(doc.getDoctor().getId());
                        tmpList.add(doc.getWeekNumber());
                        doctorScheduleMap.put(doc.getDoctor().getId(), tmpList);
                    }
                });

        //Удалить после тестов
        System.out.println(doctorScheduleMap);
    }

    public int getStartWeek() {
        initialize();
        return startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }
}
