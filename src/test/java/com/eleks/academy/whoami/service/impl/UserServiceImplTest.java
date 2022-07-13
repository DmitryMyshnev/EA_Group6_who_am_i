package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.exception.CreateUserException;
import com.eleks.academy.whoami.db.model.RegistrationToken;
import com.eleks.academy.whoami.repository.RegistrationTokenRepository;
import com.eleks.academy.whoami.repository.UserRepository;
import com.eleks.academy.whoami.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.MailSendException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserRepository userRepository;
    private RegistrationTokenRepository tokenRepository;
    private EmailService emailService;
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        userRepository = Mockito.mock(UserRepository.class);
        tokenRepository = Mockito.mock(RegistrationTokenRepository.class);
        emailService = Mockito.mock(EmailService.class);
        userService = new UserServiceImpl(userRepository, tokenRepository, emailService);
    }

    @Test
    void givenCreateUserCommand_confirmRegistration_existEmailInDBShouldThrowException() {
        var createUserCommand = new CreateUserCommand();
        createUserCommand.setEmail("email");

        when(userRepository.findByEmail(anyString())).thenThrow(CreateUserException.class);

        assertThrows(CreateUserException.class, () -> userService.confirmRegistration(createUserCommand));
    }

    @Test
    void givenCreateUserCommand_confirmRegistration_existUserTokenInDBShouldThrowException() {
        var createUserCommand = new CreateUserCommand();
        createUserCommand.setName("Pol");
        createUserCommand.setEmail("email");

        when(tokenRepository.findById(anyString())).thenThrow(CreateUserException.class);

        assertThrows(CreateUserException.class, () -> userService.confirmRegistration(createUserCommand));
    }

    @Test
    void givenCreateUserCommand_confirmRegistration_notSendMailShouldThrowException() {
        var createUserCommand = new CreateUserCommand();
        createUserCommand.setName("Pol");
        createUserCommand.setEmail("email");

        doThrow(MailSendException.class).when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());

        assertThrows(CreateUserException.class, () -> userService.confirmRegistration(createUserCommand));
    }

    @Test
    void givenToken_save_notExistShouldThrowException() {
        when(tokenRepository.findById(anyString())).thenThrow(CreateUserException.class);

        assertThrows(CreateUserException.class, () -> userService.save("token"));
    }

    @Test
    void givenNoActualToken_save_ShouldThrowException() {
        var registrationToken = new RegistrationToken("token", System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(31));

        when(tokenRepository.findById(anyString())).thenReturn(Optional.of(registrationToken));

        assertThrows(CreateUserException.class, () -> userService.save("token"));
    }
}