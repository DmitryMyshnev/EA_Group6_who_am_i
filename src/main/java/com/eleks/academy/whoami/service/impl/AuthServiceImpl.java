package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.db.dto.CredentialRequest;
import com.eleks.academy.whoami.db.dto.JwtResponse;
import com.eleks.academy.whoami.db.dto.RefreshTokenCommandDto;
import com.eleks.academy.whoami.db.dto.RefreshTokenResponse;
import com.eleks.academy.whoami.db.model.RefreshToken;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.repository.RefreshTokenRepository;
import com.eleks.academy.whoami.security.TokenBlackList;
import com.eleks.academy.whoami.security.exception.NotFoundOauthException;
import com.eleks.academy.whoami.security.exception.TokenRefreshException;
import com.eleks.academy.whoami.security.jwt.Jwt;
import com.eleks.academy.whoami.service.AuthService;
import com.eleks.academy.whoami.service.RefreshTokenService;
import com.eleks.academy.whoami.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.eleks.academy.whoami.security.AuthTokenFilter.BEARER;
import static java.lang.Boolean.FALSE;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private static final String BAD_CREDENTIAL_ERROR_MESSAGE = "Bad credential";
    private static final String IS_NOT_ACTIVATED_ERROR_MESSAGE = "Account is not activated";
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final Jwt jwt;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlackList tokenBlackList;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Override
    @Transactional
    public JwtResponse authenticate(CredentialRequest request) {
        var user = findByEmailAndPassword(request.getEmail(), request.getPassword());
        var accessToken = jwt.generateToken(user.getEmail(), accessTokenExpiration);
        var refreshToken = refreshTokenService.createRefreshToken(user);
        tokenBlackList.remove(user.getEmail());

        return JwtResponse.builder()
                .userId(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .token(accessToken)
                .type(BEARER)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenCommandDto refreshTokenCommandDto) {
        return refreshTokenRepository.findByToken(refreshTokenCommandDto.getRefreshToken())
                .filter(refreshTokenService::verifyToken)
                .map(RefreshToken::getUser)
                .map(user -> {
                    var token = jwt.generateToken(user.getEmail(), accessTokenExpiration);
                    return new RefreshTokenResponse(token);
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found"));
    }

    private User findByEmailAndPassword(String email, String password) {
        var user = (User) userService.loadUserByUsername(email);
        if (FALSE.equals(user.getIsActivated())) {
            throw new NotFoundOauthException(IS_NOT_ACTIVATED_ERROR_MESSAGE);
        }
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        throw new NotFoundOauthException(BAD_CREDENTIAL_ERROR_MESSAGE);
    }
}
