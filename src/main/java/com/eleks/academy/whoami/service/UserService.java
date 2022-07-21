package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByEmail(String  email);

    User save(String token);

   void confirmRegistration(CreateUserCommand command);
}
