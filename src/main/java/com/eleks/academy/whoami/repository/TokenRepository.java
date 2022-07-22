package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.db.model.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RegistrationToken, String> {

    Optional<RegistrationToken> findByToken(String token);
}
