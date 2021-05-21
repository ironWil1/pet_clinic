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
import com.vet24.web.util.EnvironmentUtil;
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
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Tag(name = "registration-controller", description = "operations with creation of new clients")
public class RegistrationController {


    private static  final String confirmationAPI ="api/auth/submit";
    private static final String invalidTokenMsg = "Registration token is invalid";
    private static final String passwordsInvalidMsg = "Passwords don't match";
    @Value("${registration.repeated.error.msg}")
    private   String repeatedRegistrationMsg;
    private final ClientService clientService;
    private final MailService mailService;
    private final ClientMapper clientMapper;
    private final VerificationService verificationService;
    private final EnvironmentUtil environmentUtil;


    public RegistrationController(ClientService clientService, MailService mailService, ClientMapper clientMapper, VerificationService verificationService, EnvironmentUtil environmentUtil) {
        this.clientService = clientService;
        this.mailService = mailService;
        this.clientMapper = clientMapper;
        this.verificationService = verificationService;
        this.environmentUtil = environmentUtil;
    }

    @Operation(summary = "Register new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new client"),
            @ApiResponse(responseCode = "417", description = "MessagingException"),
            @ApiResponse(responseCode = "406", description = "Inconsistent input data"),
            @ApiResponse(responseCode = "400", description = "DataIntegrityViolationException")
    })

    @PostMapping("/api/registration")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto
                                                     inputDto, Errors errors,
                                         HttpServletRequest request)throws IOException, MessagingException {
        if (errors.hasErrors()) {
            String mesBuild = errors.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
            throw new BadRequestException(mesBuild);
        }
        if(!inputDto.getPassword().equals(inputDto.getConfirmPassword())){
            throw new BadRequestException(passwordsInvalidMsg);
        }

        Client foundOrNew = clientService.getClientByEmail(inputDto.getEmail());
        if(foundOrNew == null){
            foundOrNew = clientMapper.registerDtoToClient(inputDto);
        }
        else if(foundOrNew.getRole().getName()!=RoleNameEnum.UNVERIFIED_CLIENT) {
            throw new RepeatedRegistrationException(repeatedRegistrationMsg);
        }

        foundOrNew.setRole(new Role(RoleNameEnum.UNVERIFIED_CLIENT));

        String tokenLink = environmentUtil.getServerUrlPrefix() +
                request.getContextPath() +
                RegistrationController.confirmationAPI +
                "?userCode=" +
                verificationService.createVerificationTokenDisplayCode(foundOrNew);

        mailService.sendWelcomeMessage(inputDto.getEmail(),inputDto.getFirstname(), tokenLink);

        return new  ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Confirm email of a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "Successfully verified new client"),
            @ApiResponse(responseCode = "400", description = "BadRequestException"),
            @ApiResponse(responseCode = "204", description = "Client had been already verified before. Nothing to be done.")
    })

    @GetMapping(confirmationAPI)
    public ResponseEntity<ClientDto> verifyMail(@RequestParam String userCode) {

        VerificationToken token = verificationService.getVerificationToken(userCode);

        if (token == null) {
            throw new BadRequestException(invalidTokenMsg);
        }
        Client cl = token.getClient();
        if (cl.getRole().getName() == RoleNameEnum.UNVERIFIED_CLIENT) {
            cl.setRole(new Role(RoleNameEnum.CLIENT));
            return  ResponseEntity.status(HttpStatus.RESET_CONTENT).body(
                    clientMapper.clientToClientDto(clientService.update(cl)));
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}

