package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.db.dto.CredentialRequest;
import com.eleks.academy.whoami.db.dto.JwtResponse;
import com.eleks.academy.whoami.db.dto.RefreshTokenCommandDto;
import com.eleks.academy.whoami.db.dto.RefreshTokenResponse;
import com.eleks.academy.whoami.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> signIn(@RequestBody @Valid CredentialRequest credential) {
        var jwtResponse = authService.authenticate(credential);
        return ResponseEntity.ok(jwtResponse);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenCommandDto dto) {
        var refreshToken = authService.refreshToken(dto);
        return ResponseEntity.ok(refreshToken);
    }
}
