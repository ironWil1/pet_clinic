package com.vet24.web.controllers.medicine;

import com.vet24.models.dto.medicine.DosageRequestDto;
import com.vet24.models.dto.medicine.DosageResponseDto;
import com.vet24.models.dto.medicine.MedicineRequestDto;
import com.vet24.models.dto.medicine.MedicineResponseDto;
import com.vet24.models.mappers.medicine.DosageRequestMapper;
import com.vet24.models.mappers.medicine.DosageResponseMapper;
import com.vet24.models.mappers.medicine.MedicineRequestMapper;
import com.vet24.models.mappers.medicine.MedicineResponseMapper;
import com.vet24.models.medicine.Dosage;
import com.vet24.models.medicine.Medicine;
import com.vet24.service.medicine.DosageService;
import com.vet24.service.medicine.MedicineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("api/manager/medicine")
public class MedicineController {

    private final MedicineService medicineService;
    private final MedicineResponseMapper medicineResponseMapper;
    private final MedicineRequestMapper medicineRequestMapper;
    private final DosageRequestMapper dosageRequestMapper;
    private final DosageResponseMapper dosageResponseMapper;
    private final DosageService dosageService;

    @Autowired
    public MedicineController(MedicineService medicineService, MedicineResponseMapper medicineResponseMapper, MedicineRequestMapper medicineRequestMapper, DosageRequestMapper dosageRequestMapper, DosageResponseMapper dosageResponseMapper, DosageService dosageService) {
        this.medicineService = medicineService;
        this.medicineResponseMapper = medicineResponseMapper;
        this.medicineRequestMapper = medicineRequestMapper;
        this.dosageRequestMapper = dosageRequestMapper;
        this.dosageResponseMapper = dosageResponseMapper;
        this.dosageService = dosageService;
    }

    @Operation(summary = "Поиск Препарата по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Препарат найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicineResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Препарат не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponseDto> getById(@PathVariable("id") Long id){
        Medicine medicine = medicineService.getByKey(id);
        if (medicine != null) {
            return new ResponseEntity<>(medicineResponseMapper.toDto(medicine), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Удаление Препарата по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Препарат удален"),
            @ApiResponse(responseCode = "404", description = "Препарат не найден")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        Medicine medicine = medicineService.getByKey(id);
        if (medicine != null) {
            medicineService.delete(medicine);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Изменение Препарата по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Препарат изменен"),
            @ApiResponse(responseCode = "404", description = "Препарат не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponseDto> update(@Valid @RequestBody MedicineRequestDto medicineRequestDto,
                                                      @PathVariable("id") Long id) {
        Medicine medicine = medicineService.getByKey(id);
        if (medicine == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicineRequestMapper.updateEntity(medicineRequestDto, medicine);
        medicineService.update(medicine);
        return new ResponseEntity<>(medicineResponseMapper.toDto(medicine), HttpStatus.OK);
    }

    @Operation(summary = "Создание нового Препарата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Новый Препарат бьл добавлен"),
            @ApiResponse(responseCode = "400", description = "При создании нового Препарата возникла ошибка")
    })
    @PostMapping(value = "")
    public ResponseEntity<MedicineResponseDto> save(@Valid @RequestBody MedicineRequestDto medicineRequestDto) {
        Medicine medicine = medicineRequestMapper.toEntity(medicineRequestDto);
        try {
            medicineService.persist(medicine);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(medicineResponseMapper.toDto(medicine), HttpStatus.CREATED);
    }

    @Operation(summary = "Поиск спипска Препаратов")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Препараты найдены")})
    @GetMapping("")
    public ResponseEntity<List<MedicineResponseDto>> getEntityListByParams(
            @RequestParam(required = false, name = "manufactureName", defaultValue = "") String manufactureName,
            @RequestParam(required = false, name = "name", defaultValue = "") String name,
            @RequestParam(required = false, name = "searchText", defaultValue = "") String searchText) {
        List<Medicine> medicineList = medicineService.searchFull(manufactureName, name, searchText);
        List<MedicineResponseDto> medicineDtoList = medicineResponseMapper.toDto(medicineList);
        return new ResponseEntity<>(medicineDtoList, HttpStatus.OK);
    }

    //Работа с дозировкой

    @Operation(summary = "Поиск списка Дозировок")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Дозировки найдены")})
    @GetMapping("/{medicineId}/dosage")
    public ResponseEntity<List<DosageResponseDto>> getAllDosages(@PathVariable Long medicineId) {
        List<Dosage> dosageList = dosageService.getByMedicineId(medicineId);
        List<DosageResponseDto> dosageDtoList = dosageResponseMapper.toDto(dosageList);
        return new ResponseEntity<>(dosageDtoList, HttpStatus.OK);
    }


    @Operation(summary = "Создание новой Дозировки")
    @ApiResponse(responseCode = "201", description = "Новая Дозировка была добавлена")
    @ApiResponse(responseCode = "400", description = "Данная Дозировка уже существует")
    @PostMapping("/{medicineId}/dose")
    public ResponseEntity<Void> save(@PathVariable Long medicineId, @RequestBody DosageRequestDto dosageDto) {
        Medicine medicine = medicineService.getByKey(medicineId);
        Dosage dosage = dosageRequestMapper.toEntity(dosageDto);

        if(!dosageService.isDosageExists(dosageDto.getDosageType().name(), dosageDto.getDosageSize())) {
            dosageService.persist(dosage);
            medicine.addDosage(dosage);
            medicineService.update(medicine);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Удаление Дозировки по ID")
    @ApiResponse(responseCode = "200", description = "Дозировка удалена")
    @ApiResponse(responseCode = "404", description = "Дозировка или препарат не найдены")
    @DeleteMapping(value = "/{medicineId}/dosage/{dosageId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long medicineId, @PathVariable Long dosageId) {
        Medicine medicine = medicineService.getByKey(medicineId);
        Dosage dosage = dosageService.getByKey(dosageId);

        if (medicine == null || dosage == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        medicine.removeDosage(dosage);
        medicineService.update(medicine);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}



