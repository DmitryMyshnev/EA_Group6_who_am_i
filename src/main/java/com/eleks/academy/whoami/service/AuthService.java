package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CredentialRequest;
import com.eleks.academy.whoami.db.dto.JwtResponse;
import com.eleks.academy.whoami.db.dto.RefreshTokenCommandDto;
import com.eleks.academy.whoami.db.dto.RefreshTokenResponse;

public interface AuthService {

    JwtResponse authenticate(CredentialRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenCommandDto refreshTokenCommandDto);
}
