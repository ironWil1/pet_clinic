//package com.vet24.web.controllers.pet.procedure;
//
//import com.vet24.models.dto.OnCreate;
//import com.vet24.models.dto.OnUpdate;
//import com.vet24.models.dto.exception.ExceptionDto;
//import com.vet24.models.dto.pet.procedure.DewormingDto;
//import com.vet24.models.enums.ProcedureType;
//import com.vet24.models.exception.BadRequestException;
//import com.vet24.models.mappers.pet.procedure.AbstractNewProcedureMapper;
//import com.vet24.models.mappers.pet.procedure.DewormingMapper;
//import com.vet24.models.medicine.Medicine;
//import com.vet24.models.pet.Pet;
//import com.vet24.models.pet.procedure.Deworming;
//import com.vet24.models.user.Client;
//import com.vet24.service.medicine.MedicineService;
//import com.vet24.service.pet.PetService;
//import com.vet24.service.pet.procedure.EchinococcusProcedureService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.webjars.NotFoundException;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;
//
//@RestController
//@Slf4j
//@RequestMapping("api/client/procedure/deworming")
//@Tag(name = "deworming-controller", description = "operations with Procedures")
//public class DewormingController {
//
//    private final PetService petService;
//    private final EchinococcusProcedureService echinococcusProcedureService;
//    private final DewormingMapper dewormingMapper;
//    private final AbstractNewProcedureMapper newProcedureMapper;
//    private final MedicineService medicineService;
//
//    private static final String PET_NOT_FOUND = "pet not found";
//    private static final String PROCEDURE_NOT_FOUND = "deworming procedure not found";
//    private static final String PET_NOT_YOURS = "pet not yours";
//    private static final String PROCEDURE_NOT_YOURS = "procedure not yours";
//
//    @Autowired
//    public DewormingController(PetService petService, EchinococcusProcedureService echinococcusProcedureService,
//                               AbstractNewProcedureMapper newProcedureMapper, DewormingMapper dewormingMapper,
//                               MedicineService medicineService) {
//        this.petService = petService;
//        this.echinococcusProcedureService = echinococcusProcedureService;
//
//        this.dewormingMapper = dewormingMapper;
//        this.newProcedureMapper = newProcedureMapper;
//        this.medicineService = medicineService;
//    }
//
//    @Operation(summary = "get a deworming procedure by pet id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully get a deworming procedure",
//                    content = @Content(schema = @Schema(implementation = DewormingDto.class))),
//            @ApiResponse(responseCode = "404", description = "Pet not found",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
//            @ApiResponse(responseCode = "400", description = "Pet not yours",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
//    })
//    @GetMapping("")
//    public ResponseEntity<List<DewormingDto>> getByPetId(@RequestParam Long petId) {
//
//        Pet pet = petService.getByKey(petId);
//
//        petCheck(pet);
//
//        List<Deworming> dewormingList = pet.getProcedures().stream()
//                .filter(p -> p.type == ProcedureType.ECHINOCOCCUS)
//                .map(p -> (Deworming) p)
//                .collect(Collectors.toList());
//
//        List<DewormingDto> dewormingDtoList = dewormingMapper.toDto(dewormingList);
//
//        if (dewormingList.isEmpty()) {
//            log.info("We don't have deworming procedures for petId {}", petId);
//        } else {
//            log.info("We have this deworming procedures {}", dewormingList.stream()
//                    .map(p -> Long.toString(p.getId()))
//                    .collect(Collectors.joining(","))
//            );
//        }
//
//        return new ResponseEntity<>(dewormingDtoList, HttpStatus.OK);
//    }
//
//    @Operation(summary = "get a deworming procedure by id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully get a deworming procedure",
//                    content = @Content(schema = @Schema(implementation = DewormingDto.class))),
//            @ApiResponse(responseCode = "404", description = "Deworming procedure not found",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
//            @ApiResponse(responseCode = "400", description = "Procedure not yours",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
//    })
//    @GetMapping("/{procedureId}")
//    public ResponseEntity<DewormingDto> getById(@PathVariable Long procedureId) {
//
//        Deworming deworming = echinococcusProcedureService.getByKey(procedureId);
//
//        procedureCheck(deworming);
//
//        DewormingDto dewormingDto = dewormingMapper.toDto(deworming);
//        log.info("We have this deworming procedure {}", procedureId);
//
//        return new ResponseEntity<>(dewormingDto, HttpStatus.OK);
//    }
//
//    @Operation(summary = "add a new deworming procedure")
//
//    @PostMapping("")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully added a new deworming procedure",
//                    content = @Content(schema = @Schema(implementation = DewormingDto.class))),
//            @ApiResponse(responseCode = "404", description = "Pet not found",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
//            @ApiResponse(responseCode = "400", description = "Pet not yours",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
//    })
//    public ResponseEntity<DewormingDto> save(@RequestParam Long petId, @Validated(OnCreate.class)
//    @RequestBody DewormingDto dewormingDto) {
//
//        Pet pet = petService.getByKey(petId);
//        petCheck(pet);
//
//        Deworming deworming = (Deworming) newProcedureMapper.toEntity(dewormingDto);
//
//        Medicine medicine = medicineService.getByKey(dewormingDto.getMedicineId());
//        deworming.setMedicine(medicine);
//        deworming.setPet(pet);
//        echinococcusProcedureService.persist(deworming);
//
//        pet.addProcedure(deworming);
//        petService.update(pet);
//        log.info("We added deworming procedure with this id {}", deworming.getId());
//
//        return new ResponseEntity<>(dewormingMapper.toDto(deworming), HttpStatus.CREATED);
//    }
//
//    @Operation(summary = "update a deworming procedure")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully updated a deworming procedure",
//                    content = @Content(schema = @Schema(implementation = DewormingDto.class))),
//            @ApiResponse(responseCode = "404", description = "Deworming procedure not found",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
//            @ApiResponse(responseCode = "400", description = "Procedure not yours",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
//    })
//    @PutMapping("/{procedureId}")
//    public ResponseEntity<DewormingDto> update(@PathVariable Long procedureId,
//                                               @Validated(OnUpdate.class) @RequestBody DewormingDto dewormingDto) {
//
//        Deworming deworming = echinococcusProcedureService.getByKey(procedureId);
//
//        procedureCheck(deworming);
//
//        dewormingMapper.updateEntity(dewormingDto, deworming);
//        Medicine medicine = medicineService.getByKey(dewormingDto.getMedicineId());
//        deworming.setMedicine(medicine);
//        echinococcusProcedureService.update(deworming);
//        log.info("We updated deworming procedure with this id {}", deworming.getId());
//
//        return new ResponseEntity<>(dewormingMapper.toDto(deworming), HttpStatus.OK);
//    }
//
//    @Operation(summary = "delete a deworming procedure")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully deleted a deworming procedure"),
//            @ApiResponse(responseCode = "404", description = "Deworming procedure not found",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
//            @ApiResponse(responseCode = "400", description = "Procedure not yours",
//                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
//    })
//    @DeleteMapping("/{procedureId}")
//    public ResponseEntity<Void> deleteById(@PathVariable Long procedureId) {
//
//        Deworming deworming = echinococcusProcedureService.getByKey(procedureId);
//
//        procedureCheck(deworming);
//
//        echinococcusProcedureService.delete(deworming);
//
//        Pet pet = deworming.getPet();
//        pet.removeProcedure(deworming);
//        petService.update(pet);
//        log.info("We deleted deworming procedure with this id {}", deworming.getId());
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    //TODO add validationUtil
//    private void procedureCheck(Deworming deworming) {
//        Client client = (Client) getSecurityUserOrNull();
//        if (deworming == null) {
//            throw new NotFoundException(PROCEDURE_NOT_FOUND);
//        }
//
//        Pet pet = deworming.getPet();
//
//        if (!pet.getClient().getId().equals(client.getId())) {
//            throw new BadRequestException(PROCEDURE_NOT_YOURS);
//        }
//    }
//
//    //TODO add validationUtil
//    private void petCheck(Pet pet) {
//        Client client = (Client) getSecurityUserOrNull();
//        if (pet == null) {
//            throw new NotFoundException(PET_NOT_FOUND);
//        }
//        if (!pet.getClient().getId().equals(client.getId())) {
//            throw new BadRequestException(PET_NOT_YOURS);
//        }
//    }
//}
