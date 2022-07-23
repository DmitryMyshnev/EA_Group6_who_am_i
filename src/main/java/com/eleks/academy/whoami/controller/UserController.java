package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.db.dto.CreateUserCommandDto;
import com.eleks.academy.whoami.db.dto.UserDto;
import com.eleks.academy.whoami.db.exception.NotMatchesPasswordException;
import com.eleks.academy.whoami.db.mapper.UserMapper;
import com.eleks.academy.whoami.model.request.ChangePasswordCredential;
import com.eleks.academy.whoami.model.request.EmailRequest;
import com.eleks.academy.whoami.model.request.RestorePasswordCredential;
import com.eleks.academy.whoami.model.request.UsernameRequest;
import com.eleks.academy.whoami.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static com.eleks.academy.whoami.security.AuthTokenFilter.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    @Operation(summary = "Registration new Player in system")
    public void createUser(@RequestBody @Valid CreateUserCommandDto createUserDto) {
        var createCommand = userMapper.toModel(createUserDto);
        userService.confirmRegistration(createCommand);
    }

    @Transactional
    @GetMapping("/confirm")
    @Operation(summary = "Confirmation account via email")
    public ResponseEntity<UserDto> confirmRegistration(@RequestParam String token) {
        var user = userService.save(token);
        var userDto = userMapper.toDTO(user);
        return Optional.of(userDto)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/password-restore")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMailRestorePassword(@RequestBody @Valid EmailRequest emailRequest) {
        userService.sendMailRestorePassword(emailRequest.getEmail());
    }

    @PutMapping("/access")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restorePassword(@RequestBody @Valid RestorePasswordCredential credential) {
        if (!credential.getNewPassword().equals(credential.getConfirmPassword())) {
            throw new NotMatchesPasswordException("Passwords do not match");
        }
        userService.restorePassword(
                credential.getNewPassword(),
                credential.getConfirmToken());
    }

    @Transactional
    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader(AUTHORIZATION) String token) {
        userService.logout(token.split(BEARER)[1]);
    }

    @Transactional
    @PutMapping("/{id}/name")
    public ResponseEntity<UserDto> changeUsername(@RequestBody @Valid UsernameRequest request,
                                                  @PathVariable Long id) {
        var user = userService.changeUsername(id, request.getUsername());
        var userDto = userMapper.toDTO(user);
        return Optional.of(userDto)
                .map(dto -> ResponseEntity.ok().body(dto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Transactional
    @PutMapping("/{id}/password")
    public void changePassword(@PathVariable Long id,
                               @RequestBody @Valid ChangePasswordCredential credential) {
        userService.changePassword(credential, id);
    }
}
