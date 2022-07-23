package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.exception.ChangePasswordException;
import com.eleks.academy.whoami.db.exception.CreateUserException;
import com.eleks.academy.whoami.db.exception.NotMatchesPasswordException;
import com.eleks.academy.whoami.db.exception.TokenException;
import com.eleks.academy.whoami.db.exception.NotFoundUserException;
import com.eleks.academy.whoami.db.model.RegistrationToken;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.model.request.ChangePasswordCredential;
import com.eleks.academy.whoami.repository.RefreshTokenRepository;
import com.eleks.academy.whoami.repository.TokenRepository;
import com.eleks.academy.whoami.repository.UserRepository;
import com.eleks.academy.whoami.security.jwt.Jwt;
import com.eleks.academy.whoami.service.EmailService;
import com.eleks.academy.whoami.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final Jwt jwt;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${confirm-url}")
    private String confirmUrl;


    @Value("${server.servlet.context-path}")
    private String postfix;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
    }

    @Transactional
    @Override
    public User save(String token) {
        tokenRepository.findByToken(token)
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
    @Transactional
    public void confirmRegistration(CreateUserCommand command) {
        userRepository.findByEmail(command.getEmail())
                .ifPresent(then -> {
                    throw new CreateUserException("User with email '" + command.getEmail() + "' already exist");
                });

        var userData = command.getName()
                .concat("|")
                .concat(command.getEmail());

        var token = Base64.getEncoder().encodeToString(userData.getBytes());
        tokenRepository.findByToken(token)
                .map(registrationToken -> this.getEmailByToken(registrationToken.getToken()))
                .filter(email -> email.equals(command.getEmail()))
                .ifPresent(then -> {
                    throw new CreateUserException("Not available");
                });
        var user = User.builder()
                .name(command.getName())
                .email(command.getEmail())
                .password(encoder.encode(command.getPassword()))
                .isActivated(false)
                .build();
        userRepository.save(user);
        var createTokenTime = Instant.now();
        var formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tokenRepository.save(new RegistrationToken(token, createTokenTime));
        var urlToken = generateUrl("/users/confirm?token=", token);
        String text = "Dear " + command.getName() + ", welcome WAI game.\n" +
                "To activate your account please follow the link \n" +
                urlToken +
                "\n\nThis link is actual until: " +
                formatter.format(Date.from(createTokenTime.plus(30, ChronoUnit.MINUTES)));
        emailService.sendSimpleMessage(command.getEmail(), "Confirm registration", text);
    }

    @Override
    @Transactional
    public void sendMailRestorePassword(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("User is not found"));
        var data = user.getName() + "|" + email;
        var token = Base64.getEncoder().encodeToString(data.getBytes());
        tokenRepository.findByToken(token)
                .ifPresent(tk -> tk.setCreateTime(Instant.now()));

        var urlToken = generateUrl("/users/access?token=", token);
        String text = "Dear Player, we`ve got a request to reset your WAI password.\n" +
                urlToken + "\n" +
                "If you ignore this message, your password will not be changed";
        emailService.sendSimpleMessage(email, "Confirm restore password", text);
    }

    @Override
    @Transactional
    public void restorePassword(String newPassword, String confirmToken) {
        if (!newPassword.equals(confirmToken)) {
            throw new ChangePasswordException("Passwords do not match");
        }
        var email = getEmailByToken(confirmToken);
        tokenRepository.findByToken(confirmToken)
                .or(() -> {
                    throw new TokenException("Token is not found");
                })
                .map(RegistrationToken::getCreateTime)
                .filter(savedInstant -> Instant.now().compareTo(savedInstant.plus(1, ChronoUnit.DAYS)) < 0)
                .or(() -> {
                    throw new TokenException("Token is not actual");
                })
                .flatMap(then -> userRepository.findByEmail(email))
                .ifPresent(user -> user.setPassword(encoder.encode(newPassword)));
    }

    @Override
    @Transactional
    public void logout(String token) {
        var email = jwt.getEmailFromJwtToken(token);
        userRepository.findByEmail(email)
                .ifPresent(refreshTokenRepository::deleteByUser);
    }


    @Override
    @Transactional
    public User changeUsername(Long id, String username) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(username);
                    return user;
                })
                .orElseThrow(NotFoundUserException::new);
    }

    @Override
    public void changePassword(ChangePasswordCredential credential, Long id) {
        if (!credential.getNewPassword().equals(credential.getConfirmPassword())) {
            throw new NotMatchesPasswordException("Passwords do not match");
        }

        userRepository.findById(id)
                .or(() -> {
                    throw new NotFoundUserException();
                })
                .filter(user -> !BCrypt.checkpw(credential.getNewPassword(), user.getPassword()))
                .or(() -> {
                    throw new ChangePasswordException("New password matches the old");
                })
                .map(user -> {
                    user.setPassword(encoder.encode(credential.getNewPassword()));
                    return user;
                });
    }

    private String getEmailByToken(String token) {
        String[] data;
        try {
            data = new String(Base64.getDecoder().decode(token)).split("\\|");
            if (data.length != 2) {
                throw new TokenException("Token is invalid");
            }
        } catch (IllegalArgumentException e) {
            throw new TokenException("Token is invalid");
        }
        return data[1];
    }

    private String generateUrl(String path, String token) {
        return confirmUrl + postfix + path + token;
    }
}
