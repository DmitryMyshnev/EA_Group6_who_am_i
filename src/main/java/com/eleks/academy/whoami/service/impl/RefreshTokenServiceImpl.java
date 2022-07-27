package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.model.RefreshToken;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.repository.RefreshTokenRepository;
import com.eleks.academy.whoami.security.jwt.Jwt;
import com.eleks.academy.whoami.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Jwt jwt;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;


    @Override
    public RefreshToken createRefreshToken(User user) {
        var token = new RefreshToken();
        token.setUser(user);
        token.setToken(jwt.generateToken(user.getId().toString(), refreshTokenExpiration));
        if (refreshTokenRepository.findByToken(token.getToken()).isPresent()) {
            return token;
        }
        return refreshTokenRepository.save(token);
    }

    @Override
    public boolean verifyToken(RefreshToken refreshToken) {
        return jwt.validateJwtToken(refreshToken.getToken());
    }

}
