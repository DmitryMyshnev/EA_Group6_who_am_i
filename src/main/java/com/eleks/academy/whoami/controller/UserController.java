package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.db.dto.CreateUserCommandDto;
import com.eleks.academy.whoami.db.dto.UserDto;
import com.eleks.academy.whoami.db.mapper.UserMapper;
import com.eleks.academy.whoami.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    @Operation(summary = "Registration new Player in system")
    public void createUser(@RequestBody @Valid CreateUserCommandDto createUserDto) {
        var createCommand = userMapper.toModel(createUserDto);
         userService.confirmRegistration(createCommand);
    }

    @GetMapping("/confirm")
    @Operation(summary = "Confirmation account via email")
    public ResponseEntity<UserDto> confirmRegistration(@RequestParam String token) {
        var user = userService.save(token);
        var userDto = userMapper.toDTO(user);
        return Optional.of(userDto)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
