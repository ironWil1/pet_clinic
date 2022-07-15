package com.vet24.web.controllers.pet.procedure;

import com.vet24.dao.pet.PetDaoImpl;
import com.vet24.dao.pet.procedure.ExternalParasiteProcedureDaoImpl;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.procedure.ExternalParasiteMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.ExternalParasiteProcedureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.util.List;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

@RestController
@RequestMapping("api/client/procedure/external")
@Tag(name = "Обработка от эктопаразитов - контроллер")
public class ExternalParasiteController {
    private final PetService petService;
    private final ExternalParasiteProcedureService externalParasiteProcedureService;
    private final MedicineService medicineService;
    private final ExternalParasiteMapper externalParasiteMapper;
    private final ExternalParasiteProcedureDaoImpl externalParasiteProcedureDao;
    private  final PetDaoImpl petDao;

    private static final String PET_NOT_FOUND = "Питомец не найден";
    private static final String PROCEDURE_NOT_FOUND = "Процедура не найдена";
    private static final String NOT_YOURS = "Данный питомец Вам не принадлежит";

    private void checkPet(Long petId) {
        if (!petService.isExistByKey(petId)) {
            throw new NotFoundException(PET_NOT_FOUND);
        }
    }

    private void checkProcedure(Long id) {
        if (!externalParasiteProcedureService.isExistByKey(id)) {
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }
    }

    private void checkPetOwner(Long petId) {
        if (!petDao.isPetBelongToClientByPetId(petId, getSecurityUserOrNull().getId())) {
            throw new BadRequestException(NOT_YOURS);
        }
    }

    @Autowired
    public ExternalParasiteController(PetService petService, MedicineService medicineService,
                                      ExternalParasiteMapper externalParasiteMapper,
                                      ExternalParasiteProcedureService externalParasiteProcedureService,
                                      ExternalParasiteProcedureDaoImpl externalParasiteProcedureDao, PetDaoImpl petDao) {
        this.petService = petService;
        this.externalParasiteProcedureService = externalParasiteProcedureService;
        this.medicineService = medicineService;
        this.externalParasiteMapper = externalParasiteMapper;
        this.externalParasiteProcedureDao = externalParasiteProcedureDao;
        this.petDao = petDao;
    }

    @Operation(summary = "Получить все записи питомца на обработку от эктопаразитов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены все записи по обработку от эктопаразитов",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "Записи на обработку от эктопаразитов не найдены",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Питомец не записан на обработку от эктопаразитов",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ExternalParasiteDto>> get(@RequestParam Long petId) {

        checkPet(petId);
        checkPetOwner(petId);

        List<ExternalParasiteProcedure> externalParasiteProcedureList = externalParasiteProcedureDao.getAll();

        if (externalParasiteProcedureList.isEmpty()) {
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }

        List<ExternalParasiteDto> externalParasiteDtoList = externalParasiteMapper.toDto(externalParasiteProcedureList);

        return new ResponseEntity<>(externalParasiteDtoList, HttpStatus.OK);
    }

    @Operation(summary = "Получить запись на обработку от эктопаразитов по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно найдена запись на обработку от эктопаразитов",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "Запись на обработку от эктопаразитов не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Питомец не записан на обработку от эктопаразитов",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExternalParasiteDto> getById(@PathVariable Long id) {
        checkProcedure(id);

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureDao.getByKey(id);
        ExternalParasiteDto externalParasiteDto = externalParasiteMapper.toDto(externalParasiteProcedure);

        return new ResponseEntity<>(externalParasiteDto, HttpStatus.OK);
    }

    @Operation(summary = "Записать питомца на обработку от эктопаразитов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Питомец успешно записан на обработку от эктопаразитов",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "Питомец не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Данный питомец Вам не принадлежит",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("")
    public ResponseEntity<ExternalParasiteDto> save(@RequestParam Long petId,
                                                    @RequestBody ExternalParasiteDto externalParasiteDto) {
        checkPet(petId);
        checkPetOwner(petId);

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteMapper.toEntity(externalParasiteDto);
        Pet pet = petService.getByKey(petId);

        Medicine medicine = medicineService.getByKey(externalParasiteDto.getMedicineId());
        externalParasiteProcedure.setMedicine(medicine);
        externalParasiteProcedure.setPet(pet);
        externalParasiteProcedureService.persist(externalParasiteProcedure);
        pet.addExternalParasiteProcedure(externalParasiteProcedure);
        petService.update(pet);

        return new ResponseEntity<>(externalParasiteMapper.toDto(externalParasiteProcedure), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить запись на обработку от эктопаразитов по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись на обработку от эктопаразитов успешно обновлена",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "Запись на обработку от эктопаразитов не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Питомец не записан на обработку от эктопаразитов",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExternalParasiteDto> update(@PathVariable Long id,
                                                      @Validated(OnUpdate.class)
                                                      @RequestBody ExternalParasiteDto externalParasiteDto) {
        checkProcedure(id);

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureDao.getByKey(id);
        Pet pet = externalParasiteProcedure.getPet();

        checkPet(pet.getId());
        checkPetOwner(pet.getId());

        externalParasiteMapper.updateEntity(externalParasiteDto, externalParasiteProcedure);
        Medicine medicine = medicineService.getByKey(externalParasiteDto.getMedicineId());
        externalParasiteProcedure.setMedicine(medicine);
        externalParasiteProcedureService.update(externalParasiteProcedure);

        return new ResponseEntity<>(externalParasiteMapper.toDto(externalParasiteProcedure), HttpStatus.OK);
    }

    @Operation(summary = "Удалить запись на обработку от эктопаразитов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись на обработку от эктопаразитов успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Запись на обработку от эктопаразитов не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Питомец не записан на обработку от эктопаразитов",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        checkProcedure(id);

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureService.getByKey(id);
        Pet pet = externalParasiteProcedure.getPet();

        checkPet(pet.getId());
        checkPetOwner(pet.getId());

        externalParasiteProcedureService.delete(externalParasiteProcedure);
        pet.removeExternalParasiteProcedure(externalParasiteProcedure);
        petService.update(pet);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
