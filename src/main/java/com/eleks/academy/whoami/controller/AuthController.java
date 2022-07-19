package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.db.dto.CreateUserCommandDto;
import com.eleks.academy.whoami.db.dto.JwtResponse;
import com.eleks.academy.whoami.db.dto.CredentialRequest;
import com.eleks.academy.whoami.db.dto.UserDto;
import com.eleks.academy.whoami.db.mapper.UserMapper;
import com.eleks.academy.whoami.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    public void createUser(@RequestBody @Valid CreateUserCommandDto createUserDto) {
        var createCommand = userMapper.toModel(createUserDto);
        authService.confirmRegistration(createCommand);
    }

    @Transactional
    @GetMapping("/confirm")
    public ResponseEntity<UserDto> confirmRegistration(@RequestParam String token) {
        var user = authService.save(token);
        var userDto = userMapper.toDTO(user);
        return Optional.of(userDto)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Transactional
    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> signIn(@RequestBody @Valid CredentialRequest credential) {
        var jwtResponse = authService.authenticate(credential);
        return ResponseEntity.ok(jwtResponse);
    }
}
