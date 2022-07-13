package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.exception.CreateUserException;
import com.eleks.academy.whoami.db.model.RegistrationToken;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.repository.RegistrationTokenRepository;
import com.eleks.academy.whoami.repository.UserRepository;
import com.eleks.academy.whoami.service.EmailService;
import com.eleks.academy.whoami.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final EmailService emailService;

    @Value("${confirm-url}")
    private String confirmUrl;

    @Transactional
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public User save(String token) {
        registrationTokenRepository.findByToken(token)
                .or(() -> {
                    throw new CreateUserException("Token to confirm  not found");
                })
                .map(RegistrationToken::getCreateTime)
                .filter(savedInstant -> Instant.now().compareTo(savedInstant.plus(30, ChronoUnit.MINUTES)) < 0)
                .orElseThrow(() -> new CreateUserException("Link to confirm is not actual"));

        var email = getEmailByToken(token);
        return userRepository.findByEmail(email)
                .map(user -> {
                    user.setIsActivated(true);
                    return user;
                })
                .orElseThrow(() -> new CreateUserException("User not found"));
    }

    @Override
    public void confirmRegistration(CreateUserCommand command) {
        userRepository.findByEmail(command.getEmail())
                .ifPresent(then -> {
                    throw new CreateUserException("User with email '" + command.getEmail() + "' already exist");
                });

        var userData = command.getName()
                .concat("|")
                .concat(command.getEmail());

        var token = Base64.getEncoder().encodeToString(userData.getBytes());
        registrationTokenRepository.findByToken(token)
                .map(registrationToken -> this.getEmailByToken(registrationToken.getToken()))
                .filter(email -> email.equals(command.getEmail()))
                .ifPresent(then -> {
                    throw new CreateUserException("Not available");
                });
        var user = User.builder()
                .name(command.getName())
                .email(command.getEmail())
                .password(command.getPassword())
                .isActivated(false)
                .build();
        userRepository.save(user);
        var createTokenTime = Instant.now();
        var formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        registrationTokenRepository.save(new RegistrationToken(token, createTokenTime));
        var urlToken = confirmUrl + "?token=" + token;
        String text = "Dear " + command.getName() + ", welcome WAI game.\n" +
                "To activate your account please follow the link \n" +
                urlToken +
                "\n\nThis link is actual until: " +
                formatter.format(Date.from(createTokenTime.plus(30, ChronoUnit.MINUTES)));
        emailService.sendSimpleMessage(command.getEmail(), "Confirm registration", text);
    }

    private String getEmailByToken(String token) {
        return new String(Base64
                .getDecoder()
                .decode(token))
                .split("\\|")[1];
    }
}
