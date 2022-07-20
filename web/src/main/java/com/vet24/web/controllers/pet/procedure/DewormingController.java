package com.vet24.web.controllers.pet.procedure;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.DewormingDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.procedure.DewormingMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Deworming;
import com.vet24.models.user.Client;
import com.vet24.models.util.View;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.DewormingService;
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


import java.util.List;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

@RestController
@Slf4j
@RequestMapping("api/client/procedure/deworming")
@Tag(name = "deworming-controller", description = "operations with deworming procedure")
public class DewormingController {

    private final PetService petService;
    private final DewormingService dewormingService;
    private final DewormingMapper dewormingMapper;
    private final MedicineService medicineService;

    private static final String PET_EXCEPTION = "Питомец не найден или вам не принадлежит";
    private static final String PROCEDURE_EXCEPTION = "Процедура не найдена или не принадлежит вашему питомцу";

    @Autowired
    public DewormingController(PetService petService, DewormingService dewormingService,
                               DewormingMapper dewormingMapper, MedicineService medicineService) {
        this.petService = petService;
        this.dewormingService = dewormingService;
        this.dewormingMapper = dewormingMapper;
        this.medicineService = medicineService;
    }

    @Operation(summary = "get a deworming procedure by pet id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Процедуры для питомца успешно получены",
                    content = @Content(schema = @Schema(implementation = DewormingDto.class))),
            @ApiResponse(responseCode = "400", description = PET_EXCEPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<List<DewormingDto>> getByPetId(@RequestParam Long petId) {
        petCheck(petId);
        List<Deworming> dewormings = dewormingService.getByPetId(petId);
        return new ResponseEntity<>(dewormingMapper.toDto(dewormings), HttpStatus.OK);
    }

    @Operation(summary = "get a deworming procedure by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Процедура успешно получена",
                    content = @Content(schema = @Schema(implementation = DewormingDto.class))),
            @ApiResponse(responseCode = "400", description = PROCEDURE_EXCEPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{dewormingId}")
    public ResponseEntity<DewormingDto> getById(@PathVariable Long dewormingId) {
        dewormingCheck(dewormingId);
        Deworming deworming = dewormingService.getByKey(dewormingId);
        return new ResponseEntity<>(dewormingMapper.toDto(deworming), HttpStatus.OK);
    }

    @Operation(summary = "add a new deworming procedure")
    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Процедура успешно добавлена",
                    content = @Content(schema = @Schema(implementation = DewormingDto.class))),
            @ApiResponse(responseCode = "400", description = PET_EXCEPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    public ResponseEntity<DewormingDto> save(@RequestParam Long petId,
                                             @JsonView(View.Post.class)
                                             @Validated(OnCreate.class)
                                             @RequestBody DewormingDto dewormingDto) {

        petCheck(petId);

        Deworming deworming = dewormingMapper.toEntity(dewormingDto);
        Pet pet = petService.getByKey(petId);
        Medicine medicine = medicineService.getByKey(dewormingDto.getMedicineId());
        deworming.setMedicine(medicine);
        deworming.setPet(pet);
        dewormingService.persist(deworming);

        return new ResponseEntity<>(dewormingMapper.toDto(deworming), HttpStatus.CREATED);
    }

    @Operation(summary = "update a deworming procedure")
    @PutMapping("/{dewormingId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Процедура успешно обновлена",
                    content = @Content(schema = @Schema(implementation = DewormingDto.class))),
            @ApiResponse(responseCode = "400", description = PROCEDURE_EXCEPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    public ResponseEntity<DewormingDto> update(@PathVariable Long dewormingId,
                                               @JsonView(View.Put.class)
                                               @Validated(OnUpdate.class)
                                               @RequestBody DewormingDto dewormingDto) {

        dewormingCheck(dewormingId);

        Deworming deworming = dewormingService.getByKey(dewormingId);
        dewormingMapper.updateEntity(dewormingDto, deworming);
        deworming.setId(dewormingId);
        Medicine medicine = medicineService.getByKey(dewormingDto.getMedicineId());
        deworming.setMedicine(medicine);
        dewormingService.update(deworming);

        return new ResponseEntity<>(dewormingMapper.toDto(deworming), HttpStatus.OK);
    }

    @Operation(summary = "delete a deworming procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Процедура успешно удалена"),
            @ApiResponse(responseCode = "400", description = PROCEDURE_EXCEPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @DeleteMapping("/{dewormingId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long dewormingId) {

        dewormingCheck(dewormingId);

        Deworming deworming = dewormingService.getByKey(dewormingId);
        dewormingService.delete(deworming);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO add validationUtil
    private void dewormingCheck(Long dewormingId) {
        Client client = (Client) getSecurityUserOrNull();
        if (!dewormingService.isExistByDewormingIdAndClientId(dewormingId, client.getId())) {
            throw new BadRequestException(PROCEDURE_EXCEPTION);
        }
    }

    //TODO add validationUtil
    private void petCheck(Long petId) {
        Client client = (Client) getSecurityUserOrNull();
        if (!petService.isExistByPetIdAndClientId(petId, client.getId())) {
            throw new BadRequestException(PET_EXCEPTION);
        }
    }
}
