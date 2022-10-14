package com.vet24.web.controllers.medicine;

import com.vet24.models.dto.medicine.MedicineRequestDto;
import com.vet24.models.dto.medicine.MedicineResponseDto;
import com.vet24.models.mappers.medicine.MedicineRequestMapper;
import com.vet24.models.mappers.medicine.MedicineResponseMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Dog;
import com.vet24.service.medicine.MedicineService;

import com.vet24.web.controllers.annotations.CheckExist;
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

    @Autowired
    public MedicineController(MedicineService medicineService, MedicineResponseMapper medicineResponseMapper, MedicineRequestMapper medicineRequestMapper) {
        this.medicineService = medicineService;
        this.medicineResponseMapper = medicineResponseMapper;
        this.medicineRequestMapper = medicineRequestMapper;
    }

    @Operation(summary = "Поиск Препарата по ID (добавила еще id)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Препарат найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicineResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Препарат не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponseDto> getById(
            @CheckExist(entityClass = Medicine.class) @PathVariable("id") Long id
//            , @CheckExist(entityClass = Dog.class) @RequestParam ("id1") Long id1
    ) {
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
}



