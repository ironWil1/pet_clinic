package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.RegisterDto;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.exception.RegistrationDataInconsistentException;
import com.vet24.models.mappers.user.ClientMapper;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.service.media.MailService;
import com.vet24.service.user.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/registration")
@Tag(name = "registration-controller", description = "operations with creation of new clients")
public class RegistrationController {
    private final ClientService clientService;
    private final MailService mailService;
    private final ClientMapper clientMapper;

    @Autowired
    public RegistrationController(ClientService clientService,
                                  MailService mailService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.mailService = mailService;
        this.clientMapper = clientMapper;
    }

    @Operation(summary = "Register new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new client"),
            @ApiResponse(responseCode = "417", description = "MessagingException"),
            @ApiResponse(responseCode = "406", description = "Inconsistent input data"),
            @ApiResponse(responseCode = "400", description = "DataIntegrityViolationException")
    })

    @PostMapping
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto
                                                     inputDto, Errors errors)throws IOException, MessagingException {
        if (errors.hasErrors()) {
            throw new RegistrationDataInconsistentException(errors);
        }
        if(!inputDto.getPassword().equals(inputDto.getConfirmPassword())){
            throw new RegistrationDataInconsistentException("Passwords don't match");
        }
        Client cl = clientMapper.registerDtoToClient(inputDto);
        cl.setRole(new Role(RoleNameEnum.CLIENT));
        clientService.persist(cl);
        mailService.sendWelcomeMessage(inputDto.getEmail(),inputDto.getFirstname());
        return new  ResponseEntity<>(HttpStatus.CREATED);
    }
}

