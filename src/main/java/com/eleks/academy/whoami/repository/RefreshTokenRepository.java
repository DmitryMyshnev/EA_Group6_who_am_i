package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.db.model.RefreshToken;
import com.eleks.academy.whoami.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String refreshToken);

    void deleteByUser(User user);
}
