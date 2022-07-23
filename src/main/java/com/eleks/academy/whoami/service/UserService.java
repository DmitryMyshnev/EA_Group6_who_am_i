package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(String token);

    void confirmRegistration(CreateUserCommand command);

    User findById(Long id);
}
