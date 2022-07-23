package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.model.RefreshToken;
import com.eleks.academy.whoami.db.model.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    boolean verifyToken(RefreshToken refreshToken);

}
