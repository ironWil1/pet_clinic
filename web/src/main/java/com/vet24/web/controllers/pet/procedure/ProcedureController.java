package com.vet24.web.controllers.pet.procedure;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/client/pet/{petId}/procedure")
@Tag(name = "procedure-controller", description = "operations with Procedures")
public class ProcedureController {

    private final PetService petService;
    private final ProcedureService procedureService;
    private final ProcedureMapper procedureMapper;
    private final ClientService clientService;

    @Autowired
    public ProcedureController(PetService petService, ProcedureService procedureService,
                               ProcedureMapper procedureMapper, ClientService clientService) {
        this.petService = petService;
        this.procedureService = procedureService;
        this.procedureMapper = procedureMapper;
        this.clientService = clientService;
    }

    @Operation(summary = "get a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{procedureId}")
    public ResponseEntity<ProcedureDto> getById(@PathVariable Long petId, @PathVariable Long procedureId) {
        return null;
    }

    @Operation(summary = "add a new Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a new Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("/")
    public ResponseEntity<ProcedureDto> save(@PathVariable Long petId,
                                             @RequestBody AbstractNewProcedureDto newProcedureDto) {
        return null;
    }

    @Operation(summary = "update a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("/{procedureId}")
    public ResponseEntity<ProcedureDto> update(@PathVariable Long petId, @PathVariable Long procedureId,
                                         @RequestBody ProcedureDto procedureDto) {
        return null;
    }

    @Operation(summary = "delete a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a Procedure"),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @DeleteMapping("/{procedureId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long procedureId) {
        return null;
    }
}
