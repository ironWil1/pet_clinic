package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.dto.user.RegisterDto;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.exception.RepeatedRegistrationException;
import com.vet24.models.mappers.user.ClientMapper;
import com.vet24.models.user.Client;
import com.vet24.models.user.Role;
import com.vet24.models.user.VerificationToken;
import com.vet24.service.media.MailService;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/registration")
@Tag(name = "registration-controller", description = "operations with creation of new clients")
public class RegistrationController {

    private static final String INVALID_TOKEN_MSG = "Registration token is invalid";
    private static final String PASSWORDS_UNMATCHED = "Passwords don't match";
    @Value("${registration.repeated.error.msg}")
    private  String repeatedRegistrationMsg;
    private final ClientService clientService;
    private final MailService mailService;
    private final ClientMapper clientMapper;
    private final VerificationService verificationService;

    public RegistrationController(ClientService clientService, MailService mailService,
                                  ClientMapper clientMapper, VerificationService verificationService) {
        this.clientService = clientService;
        this.mailService = mailService;
        this.clientMapper = clientMapper;
        this.verificationService = verificationService;
    }

    @Operation(summary = "Register new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new client"),
            @ApiResponse(responseCode = "417", description = "MessagingException"),
            @ApiResponse(responseCode = "406", description = "Inconsistent input data"),
            @ApiResponse(responseCode = "400", description = "DataIntegrityViolationException")
    })
    @PostMapping("")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto inputDto, Errors errors) throws IOException, MessagingException {
        if (errors.hasErrors()) {
            String mesBuild = errors.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
            log.info("All errors during registration {}",mesBuild);
            throw new BadRequestException(mesBuild);
        }
        if(!inputDto.getPassword().equals(inputDto.getConfirmPassword())){
            log.info("Password confirmation is wrong");
            throw new BadRequestException(PASSWORDS_UNMATCHED);
        }

        Client foundOrNew = clientService.getClientByEmail(inputDto.getEmail());

        if(foundOrNew == null){
            foundOrNew = clientMapper.toEntity(inputDto);
            foundOrNew.setRole(new Role(RoleNameEnum.UNVERIFIED_CLIENT));
            clientService.persist(foundOrNew);
        } else if(foundOrNew.getRole().getName()!= RoleNameEnum.UNVERIFIED_CLIENT) {
            log.info("The client with id {} have repeated registration ",foundOrNew.getId());
            throw new RepeatedRegistrationException(repeatedRegistrationMsg);
        } else {
            VerificationToken verificationToken = verificationService.findByClientId(foundOrNew.getId());
            verificationService.delete(verificationToken);
        }

        String tokenUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() +
                "/api/registration/confirm/" + verificationService.createVerificationToken(foundOrNew);
        mailService.sendWelcomeMessage(inputDto.getEmail(), inputDto.getFirstname(), tokenUrl);
        log.info("The registration for client with id {} is created ",foundOrNew.getId());
        return new  ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Confirm email of a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "Successfully verified new client"),
            @ApiResponse(responseCode = "400", description = "BadRequestException")
    })
    @GetMapping("/confirm/{token}")
    public ResponseEntity<ClientDto> verifyMail(@PathVariable(value="token") String token/* @RequestParam String userCode*/) {

        VerificationToken verificationToken = verificationService.getVerificationToken(token);
        if (verificationToken == null) {
            throw new BadRequestException(INVALID_TOKEN_MSG);
        }
        Client cl = verificationToken.getClient();
        cl.setRole(new Role(RoleNameEnum.CLIENT));
        ClientDto clientUpdated = clientMapper.toDto(clientService.update(cl));
        verificationService.delete(verificationToken);
        log.info("The client with mail {} is updated ",clientUpdated.getEmail());
        return  ResponseEntity.status(HttpStatus.RESET_CONTENT).body(clientUpdated);
    }
}

