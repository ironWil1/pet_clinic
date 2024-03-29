package com.vet24.service.medicine;

import com.vet24.models.enums.WorkShift;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.user.User;
import com.vet24.service.user.DoctorNonWorkingService;
import com.vet24.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DoctorScheduleBalancerImpl implements DoctorScheduleBalancer {
    private final DoctorScheduleService doctorScheduleService;
    private final DoctorNonWorkingService doctorNonWorkingService;
    private final UserService userService;

    private LocalDate localDate = LocalDate.now();
    private LocalDate currentData;
    private List<DoctorSchedule> doctorScheduleList;
    private Map<Long, List<Integer>> doctorNonWorkingMap;
    private Map<Long, Set<Map<LocalDate, String>>> doctorScheduleMap;
    private int countWorkWeekFirstShift;
    private int countWorkWeekSecondShift;

    @Autowired
    public DoctorScheduleBalancerImpl(DoctorScheduleService doctorScheduleService,
                                      DoctorNonWorkingService doctorNonWorkingService,
                                      UserService userService) {
        this.doctorScheduleService = doctorScheduleService;
        this.doctorNonWorkingService = doctorNonWorkingService;
        this.userService = userService;
    }

    /**
     * Проверка доктора, болен ли он на этой неделе.
     */
    private boolean doctorNonWorkingByIdAndWeek(Long doctorId, LocalDate date) {
        int week = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        return doctorNonWorkingMap.containsKey(doctorId) && doctorNonWorkingMap.get(doctorId).contains(week);
    }

    /**
     * Проверка есть ли смена у доктора в текущей неделе
     */
    private boolean doctorScheduleExistsByIdAndLocalDate(Long doctorId, LocalDate date) {
        if (doctorScheduleMap.containsKey(doctorId)) {
            Set<Map<LocalDate, String>> list = doctorScheduleMap.get(doctorId);
            for (Map<LocalDate, String> map : list) {
                if (map.containsKey(date)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Получаем смену у доктора(doctorId) по переданной дате
     */
    private String getShiftByLocalDate(Long doctorId, LocalDate date) {
        if (doctorScheduleMap.containsKey(doctorId)) {
            Set<Map<LocalDate, String>> list = doctorScheduleMap.get(doctorId);
            String shift;
            for (Map<LocalDate, String> map : list) {
                if (map.containsKey(date)) {
                    shift = map.get(date);
                    return shift;
                }
            }
        }
        return "";
    }

    /**
     * Корректируем любой день в понедельник будущей недели
     */
    private LocalDate setFirstDayOfWeek(LocalDate tmpDate) {
        if (tmpDate.getDayOfWeek().getValue() != 1) {
            return tmpDate.plusDays((8 - tmpDate.getDayOfWeek().getValue()));
        } else {
            return tmpDate;
        }
    }

    /**
     * Стартовая инициализация мап, листов, даты
     */
    private void initialize() {
        doctorNonWorkingMap = new HashMap<>();
        doctorScheduleMap = new HashMap<>();

        localDate = setFirstDayOfWeek(localDate);
        currentData = LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());

        //Получаем всех неработающих докторов начиная от текущей даты вперед, переводим в Map
        doctorNonWorkingService.getDoctorNonWorkingAfterDate(localDate)
                .stream()
                .forEach(doc -> addDoctorNonWorkingToMap(doc.getDoctor().getId(), doc.getDate()));

        //Получение всех смен докторов за месяц
        //Заполняем doctorScheduleMap -  Map <ID доктора, Set <Map<дата понедельника, смена>>
        doctorScheduleService.getDoctorScheduleAfterDate(localDate.minusWeeks(4))
                .stream()
                .forEach(docsched -> addDoctorScheduleToMap(docsched.getDoctor().getId(), docsched.getWorkShift().toString(), docsched.getStartWeek()));
    }

    /**
     * Добавляем неработающих докторов в Map(Id доктора, List с датами)
     */
    private void addDoctorNonWorkingToMap(Long doctorId, LocalDate localDate) {
        List<Integer> tmpList;
        if (!doctorNonWorkingMap.containsKey(doctorId)) {
            tmpList = new ArrayList<>();
        } else {
            tmpList = doctorNonWorkingMap.get(doctorId);
        }
        tmpList.add(localDate.get(WeekFields.of(Locale.getDefault()).weekOfYear()));
        doctorNonWorkingMap.put(doctorId, tmpList);
    }

    /**
     * Дублируем добавление доктора в Map  одновременно с doctorScheduleList с текущей неделей,
     * для проверки чередования смен на будущей неделе
     */
    private void addDoctorScheduleToMap(Long doctorId, String workShift, LocalDate date) {
        if (!doctorScheduleMap.containsKey(doctorId)) {
            Map<LocalDate, String> tmpMap = new HashMap<>();
            Set<Map<LocalDate, String>> tmpList = new HashSet<>();
            tmpMap.put(date, workShift);
            tmpList.add(tmpMap);
            doctorScheduleMap.put(doctorId, tmpList);
        } else {
            Set<Map<LocalDate, String>> tmpList = doctorScheduleMap.get(doctorId);
            Map<LocalDate, String> tmpMap = new HashMap<>();
            tmpMap.put(date, workShift);
            tmpList.add(tmpMap);
            doctorScheduleMap.put(doctorId, tmpList);
        }
    }

    /**
     * Балансировка смен за счет вновь принятых докторов, или вышедших с нерабочих дней
     */
    private void addNewAndNonWorkingDoctor(User doctor, LocalDate date) {
        if ((countWorkWeekFirstShift < countWorkWeekSecondShift)
                && (!getShiftByLocalDate(doctor.getId(), date.minusWeeks(1)).equals("FIRST_SHIFT")
                && !getShiftByLocalDate(doctor.getId(), date.minusWeeks(2)).equals("FIRST_SHIFT"))) {
            doctorScheduleList.add(new DoctorSchedule(doctor, WorkShift.FIRST_SHIFT, date));
            addDoctorScheduleToMap(doctor.getId(), "FIRST_SHIFT", date);
            countWorkWeekFirstShift++;
        } else {
            doctorScheduleList.add(new DoctorSchedule(doctor, WorkShift.SECOND_SHIFT, date));
            addDoctorScheduleToMap(doctor.getId(), "SECOND_SHIFT", date);
            countWorkWeekSecondShift++;
        }
    }
    /**
     * Если у доктора не было смен на прошлой неделе, добавляем его в балансировку
     */

    private void addDoctorsListNonWorkingMinusOneWeekToBalance(List<User> doctorsListNonWorkingMinusOneWeek) {
        if (!doctorsListNonWorkingMinusOneWeek.isEmpty()) {
            for (int i = 0; i < doctorsListNonWorkingMinusOneWeek.size(); i++) {
                if (doctorScheduleExistsByIdAndLocalDate(doctorsListNonWorkingMinusOneWeek.get(i).getId(), localDate)) {
                    continue;
                }
                addNewAndNonWorkingDoctor(doctorsListNonWorkingMinusOneWeek.get(i), localDate);
            }
        }
    }

    private List<User> getDoctorsCurrentWeekList(List<User> doctorsList) {
        List<User> doctorsCurrentWeekList = doctorsList.stream()
                .filter(doc -> !doctorNonWorkingByIdAndWeek(doc.getId(), localDate))
                .collect(Collectors.toList());
        //Перемешиваем коллекцию, для случайного распределения смен, в принципе нужно только при начальной инициализации БД
        Collections.shuffle(doctorsCurrentWeekList);
        return doctorsCurrentWeekList;
    }

    /**
     * Основной метод для балансировки смен
     */

    @Override
    public void balance() {
        //Получение всех докторов
        List<User> doctorsList = userService.getAll();

        //Выходной лист DoctorSchedule для записи в БД
        doctorScheduleList = new ArrayList<>();

        //Стартовое заполнение карт из БД
        initialize();

        //Лист для докторов, вышедших с выходных, для балансировки
        List<User> doctorsListNonWorkingMinusOneWeek = new ArrayList<>(0);

        //Начало итераций цикла по неделям текущего месяца
        //TODO По поводу недель тоже нужно подумать, если по 4 недели прибавлять, то постепенно выходим на середину месяца
        //TODO Еще проблема с если 1 день месяца понедельник - то 30 числа предыдущего месяца будет неизвестно, кому выходить 1 числа
        // при данной реализации по окончании каждой недели, будет добавляться следующая
        for (int currentWeek = 0; currentWeek < 5; currentWeek++) {
            localDate = setFirstDayOfWeek(localDate.plusWeeks(currentWeek == 0 ? 0 : 1));
            countWorkWeekFirstShift = 0;
            countWorkWeekSecondShift = 0;

            //Загружаем докторов работающих на текущей неделе, за минусом неработающих
            List<User> doctorsCurrentWeekList = getDoctorsCurrentWeekList(doctorsList);

            for (int i = 0; i < doctorsCurrentWeekList.size(); i++) {
                //Если у доктора есть уже смена на текущей неделе добавляем счетчик смен, пропускаем цикл
                if (doctorScheduleExistsByIdAndLocalDate(doctorsCurrentWeekList.get(i).getId(), localDate)) {
                    if (getShiftByLocalDate(doctorsCurrentWeekList.get(i).getId(), localDate).equals("FIRST_SHIFT")) {
                        countWorkWeekFirstShift++;
                    } else {
                        countWorkWeekSecondShift++;
                    }
                    continue;
                }

                //Сам алгоритм балансироки
                if (getShiftByLocalDate(doctorsCurrentWeekList.get(i).getId(), localDate.minusWeeks(1)).equals("SECOND_SHIFT")) {
                    doctorScheduleList.add(new DoctorSchedule(doctorsCurrentWeekList.get(i), WorkShift.FIRST_SHIFT, localDate));
                    addDoctorScheduleToMap(doctorsCurrentWeekList.get(i).getId(), "FIRST_SHIFT", localDate);
                    countWorkWeekFirstShift++;
                } else if (getShiftByLocalDate(doctorsCurrentWeekList.get(i).getId(), localDate.minusWeeks(1)).equals("FIRST_SHIFT")) {
                    doctorScheduleList.add(new DoctorSchedule(doctorsCurrentWeekList.get(i), WorkShift.SECOND_SHIFT, localDate));
                    addDoctorScheduleToMap(doctorsCurrentWeekList.get(i).getId(), "SECOND_SHIFT", localDate);
                    countWorkWeekSecondShift++;
                } else {
                    doctorsListNonWorkingMinusOneWeek.add(doctorsCurrentWeekList.get(i));
                }
            }
            //Добавляем докторов, которые на прошлой недели не работали, за их счет балансируем примерно
            //равное их количество на текущей неделе
            addDoctorsListNonWorkingMinusOneWeekToBalance(doctorsListNonWorkingMinusOneWeek);
            doctorsListNonWorkingMinusOneWeek.clear();
        }

        doctorScheduleService.persistAll(doctorScheduleList);

        //TODO Сделал временную заглушку, нужно тестить, если без нее вызвать вручную контроллер, заполняет следующие четыре недели
        localDate = currentData;
    }


}
