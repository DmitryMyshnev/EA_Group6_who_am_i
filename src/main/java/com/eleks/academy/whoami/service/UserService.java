package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.db.dto.CreateUserCommand;
import com.eleks.academy.whoami.db.model.User;
import com.eleks.academy.whoami.model.request.ChangePasswordCredential;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(String token);

    void confirmRegistration(CreateUserCommand command);


    void sendMailRestorePassword(String email);

    void restorePassword(String newPassword, String confirmToken);

    User findById(Long id);

    void logout(String token);

    User changeUsername(Long id, String username);

    void changePassword(ChangePasswordCredential credential, Long id);
}
