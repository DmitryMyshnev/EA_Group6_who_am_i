package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.db.model.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, String> {
}
