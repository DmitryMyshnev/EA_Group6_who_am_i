package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.exception.CreateUserException;
import com.eleks.academy.whoami.db.exception.NotFoundUserException;
import com.eleks.academy.whoami.db.exception.TokenException;
import com.eleks.academy.whoami.db.model.RegistrationToken;
import com.eleks.academy.whoami.repository.TokenRepository;
import com.eleks.academy.whoami.repository.UserRepository;
import com.eleks.academy.whoami.service.EmailService;
import com.eleks.academy.whoami.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private EmailService emailService;
    private UserService userService;

    @BeforeEach
    void init() {
        userRepository = Mockito.mock(UserRepository.class);
        tokenRepository = Mockito.mock(TokenRepository.class);
        emailService = Mockito.mock(EmailService.class);
        PasswordEncoder   encoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, tokenRepository, emailService, encoder);
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

        when(tokenRepository.findByToken(anyString())).thenThrow(CreateUserException.class);

        assertThrows(CreateUserException.class, () -> userService.confirmRegistration(createUserCommand));
    }

    @Test
    void givenCreateUserCommand_confirmRegistration_notSendMailShouldThrowException() {
        var createUserCommand = new CreateUserCommand();
        createUserCommand.setName("Pol");
        createUserCommand.setEmail("email");

        doThrow(MailSendException.class).when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());

        assertThrows(MailSendException.class, () -> userService.confirmRegistration(createUserCommand));
    }

    @Test
    void givenToken_save_notExistShouldThrowException() {
        when(tokenRepository.findById(anyString())).thenThrow(CreateUserException.class);

        assertThrows(CreateUserException.class, () -> userService.save("token"));
    }

    @Test
    void givenNoActualToken_save_ShouldThrowException() {
        var registrationToken = new RegistrationToken("token", Instant.now().minus(31,ChronoUnit.MINUTES));

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(registrationToken));

        assertThrows(CreateUserException.class, () -> userService.save("token"));
    }

    @Test
    void  givenNotExistEmail_sendMailRestorePassword_shouldBeThrowException(){
        when(userRepository.findByEmail(anyString())).thenThrow(NotFoundUserException.class);
        assertThrows(NotFoundUserException.class, () -> userService.sendMailRestorePassword("test@mail"));
    }

    @Test
    void givenInvalidToken_changePassword_shouldBeThrowException(){
        assertThrows(TokenException.class,()->userService.changePassword("123", "AEWq"));
    }

@Test
    void givenNotExistToken_changePassword_shouldBeThrowException(){
    when(tokenRepository.findByToken(anyString())).thenThrow(TokenException.class);

    assertThrows(TokenException.class,()->userService.changePassword("123", "AEW|eq"));
}
}