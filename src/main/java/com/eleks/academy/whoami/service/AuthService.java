package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.dto.JwtResponse;
import com.eleks.academy.whoami.db.dto.CredentialRequest;
import com.eleks.academy.whoami.db.model.User;

public interface AuthService {

    User save(String token);

    void confirmRegistration(CreateUserCommand command);

    JwtResponse authenticate(CredentialRequest request);
}
