package com.vet24.web.controllers.pet.procedure;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.googleEvent.GoogleEventDto;
import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.notification.NotificationEventMapper;
import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
import com.vet24.models.notification.Notification;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Client;
import com.vet24.service.notification.GoogleEventService;
import com.vet24.service.notification.NotificationService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.ProcedureService;
import com.vet24.service.user.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("api/client/pet/{petId}/procedure")
@Tag(name = "procedure-controller", description = "operations with Procedures")
public class ProcedureController {

    private final PetService petService;
    private final ProcedureService procedureService;
    private final ProcedureMapper procedureMapper;
    private final ClientService clientService;
    private final NotificationService notificationService;
    private final NotificationEventMapper notificationEventMapper;
    private final GoogleEventService googleEventService;

    @Autowired
    public ProcedureController(PetService petService, ProcedureService procedureService,
                               ProcedureMapper procedureMapper, ClientService clientService,
                               NotificationEventMapper notificationEventMapper,
                               NotificationService notificationService,
                               GoogleEventService googleEventService) {
        this.petService = petService;
        this.procedureService = procedureService;
        this.procedureMapper = procedureMapper;
        this.clientService = clientService;
        this.notificationService = notificationService;
        this.notificationEventMapper = notificationEventMapper;
        this.googleEventService = googleEventService;
    }

    @Operation(summary = "get a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{procedureId}")
    public ResponseEntity<ProcedureDto> getById(@PathVariable Long petId, @PathVariable Long procedureId) {
        Client client = clientService.getCurrentClient();
        Pet pet = petService.getByKey(petId);
        Procedure procedure = procedureService.getByKey(procedureId);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (procedure == null) {
            throw new NotFoundException("procedure not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!procedure.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("pet not assigned to this procedure");
        }
        ProcedureDto procedureDto = procedureMapper.procedureToProcedureDto(procedure);

        return new ResponseEntity<>(procedureDto, HttpStatus.OK);
    }

    @Operation(summary = "add a new Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added a new Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "pet not yours OR cannot create event OR need set period days",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("")
    public ResponseEntity<ProcedureDto> save(@PathVariable Long petId,
                                             @RequestBody AbstractNewProcedureDto newProcedureDto) {
        Client client = clientService.getCurrentClient();
        Pet pet = petService.getByKey(petId);
        Procedure procedure = procedureMapper.abstractNewProcedureDtoToProcedure(newProcedureDto);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (procedure.getIsPeriodical()) {
            createProcedureNotification(procedure, pet, client);
        }

        procedureService.persist(procedure);
        pet.addProcedure(procedure);
        petService.update(pet);

        return new ResponseEntity<>(procedureMapper.procedureToProcedureDto(procedure), HttpStatus.CREATED);
    }

    @Operation(summary = "update a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("/{procedureId}")
    public ResponseEntity<ProcedureDto> update(@PathVariable Long petId, @PathVariable Long procedureId,
                                               @RequestBody ProcedureDto procedureDto) {
        Client client = clientService.getCurrentClient();
        Pet pet = petService.getByKey(petId);
        Procedure oldProcedure = procedureService.getByKey(procedureId);
        Procedure newProcedure = procedureMapper.procedureDtoToProcedure(procedureDto);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (oldProcedure == null) {
            throw new NotFoundException("procedure not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!oldProcedure.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("pet not assigned to this procedure");
        }
        if (!oldProcedure.getPet().equals(pet)) {
            throw new BadRequestException("unable to change procedure pet");
        }
        if (!procedureId.equals(newProcedure.getId())) {
            throw new BadRequestException("procedureId in path and in body not equals");
        }

        // old(not periodical) + new(periodical) -> create notification & event
        if (!oldProcedure.getIsPeriodical() && newProcedure.getIsPeriodical()) {
            createProcedureNotification(newProcedure, pet, client);
        }
        // old(periodical) + new(periodical) -> update notification & event
        if (oldProcedure.getIsPeriodical() && newProcedure.getIsPeriodical()) {
            updateProcedureNotification(oldProcedure, newProcedure, pet, client);
        }
        // old(periodical) + new(not periodical) -> delete notification & event
        if (oldProcedure.getIsPeriodical() && !newProcedure.getIsPeriodical()) {
            deleteProcedureNotification(oldProcedure, pet, client);
        }

        newProcedure.setPet(pet);
        procedureService.update(newProcedure);

        return new ResponseEntity<>(procedureMapper.procedureToProcedureDto(newProcedure), HttpStatus.OK);
    }

    @Operation(summary = "delete a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a Procedure"),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @DeleteMapping("/{procedureId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long procedureId) {
        Client client = clientService.getCurrentClient();
        Procedure procedure = procedureService.getByKey(procedureId);
        Pet pet = petService.getByKey(petId);

        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        if (procedure == null) {
            throw new NotFoundException("procedure not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("pet not yours");
        }
        if (!procedure.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("pet not assigned to this procedure");
        }

        if (procedure.getIsPeriodical()) {
            deleteProcedureNotification(procedure, pet, client);
        }

        pet.removeProcedure(procedure);
        procedureService.delete(procedure);
        petService.update(pet);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void createProcedureNotification(Procedure procedure, Pet pet, Client client) {
        if (!(procedure.getPeriodDays() > 0)) {
            throw new BadRequestException("for periodical procedure need to set period days");
        }
        Notification notification = new Notification(
                null,
                null,
                Timestamp.valueOf(LocalDateTime.of(procedure.getDate().plusDays(procedure.getPeriodDays()), LocalTime.MIDNIGHT)),
                Timestamp.valueOf(LocalDateTime.of(procedure.getDate().plusDays(procedure.getPeriodDays()), LocalTime.NOON)),
                "Periodic procedure for your pet",
                "Pet clinic 1",
                "Procedure '" + procedure.getType().name().toLowerCase() + "' \n" +
                        "for pet " + pet.getName() + " \n" +
                        "[every " + procedure.getPeriodDays() + " day(s)]",
                pet
        );
        GoogleEventDto googleEventDto = notificationEventMapper
                .notificationWithEmailToGoogleEventDto(notification, client.getEmail());

        try {
            googleEventService.createEvent(googleEventDto);
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage(), exception.getCause());
        }

        notification.setEvent_id(googleEventDto.getId());
        notificationService.persist(notification);
        procedure.setNotification(notification);
        pet.addNotification(notification);
    }

    private void updateProcedureNotification(Procedure oldProcedure, Procedure newProcedure, Pet pet, Client client) {
        if (!(newProcedure.getPeriodDays() > 0)) {
            throw new BadRequestException("for periodical procedure need to set period days");
        }
        Notification notification = new Notification(
                oldProcedure.getNotification().getId(),
                oldProcedure.getNotification().getEvent_id(),
                Timestamp.valueOf(LocalDateTime.of(newProcedure.getDate().plusDays(newProcedure.getPeriodDays()), LocalTime.MIDNIGHT)),
                Timestamp.valueOf(LocalDateTime.of(newProcedure.getDate().plusDays(newProcedure.getPeriodDays()), LocalTime.NOON)),
                "Periodic procedure for your pet",
                "Pet clinic 1",
                "Procedure '" + newProcedure.getType().name().toLowerCase() + "' \n" +
                        "for pet " + pet.getName() + " \n" +
                        "[every " + newProcedure.getPeriodDays() + " day(s)]",
                pet
        );
        GoogleEventDto googleEventDto = notificationEventMapper
                .notificationWithEmailToGoogleEventDto(notification, client.getEmail());

        try {
            googleEventService.editEvent(googleEventDto);
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage(), exception.getCause());
        }

        notificationService.update(notification);
        newProcedure.setNotification(notification);
    }

    private void deleteProcedureNotification(Procedure procedure, Pet pet, Client client) {
        if (procedure.getNotification() == null) {
            throw new BadRequestException("notification not found");
        }
        GoogleEventDto googleEventDto = new GoogleEventDto();
        googleEventDto.setId(procedure.getNotification().getEvent_id());
        googleEventDto.setEmail(client.getEmail());

        try {
            googleEventService.deleteEvent(googleEventDto);
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage(), exception.getCause());
        }

        Notification notification = procedure.getNotification();
        procedure.setNotification(null);
        pet.removeNotification(notification);
        notificationService.delete(notification);
    }
}
