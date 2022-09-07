package com.vet24.web.controllers.found;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;

import com.vet24.models.dto.pet.PetFoundClientDto;
import com.vet24.models.mappers.pet.PetFoundClientMapper;
import com.vet24.models.pet.PetFound;
import com.vet24.models.user.User;
import com.vet24.service.pet.PetFoundService;
import com.vet24.service.pet.PetService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/client/petFound")
@Tag(name = "История находок питомца")
public class PetFoundClientController {

    private final PetFoundClientMapper petFoundClientMapper;
    private final PetFoundService petFoundService;
    private final PetService petService;

    public PetFoundClientController(PetFoundClientMapper petFoundClientMapper,
                                    PetFoundService petFoundService,
                                    PetService petService) {
        this.petFoundClientMapper = petFoundClientMapper;
        this.petFoundService = petFoundService;
        this.petService = petService;
    }

    @Operation(summary = "Получение истории находок питомца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "История находок питомца успешно получена",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PetFoundClientDto.class)))),
            @ApiResponse(responseCode = "400", description = "Указан неверный id питомца"),
            @ApiResponse(responseCode = "404", description = "История находок питомца не найдена")
    })
    @GetMapping(value = "")
    public ResponseEntity<List<PetFoundClientDto>> getHistoryPetById(@RequestParam(value = "petId") Long petId) {
        Optional<User> client = getOptionalOfNullableSecurityUser();
        if (client.isEmpty() || !petService.isExistByPetIdAndClientId(petId, client.get().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!petFoundService.isExistByKey(petId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PetFound> petFounds = petFoundService.getPetFoundByPetId(petId);
        return new ResponseEntity<>(petFoundClientMapper.toDto(petFounds), HttpStatus.OK);
    }
}