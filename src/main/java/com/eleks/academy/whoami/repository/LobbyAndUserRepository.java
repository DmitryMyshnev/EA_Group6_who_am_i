package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.db.model.LobbyAndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyAndUserRepository extends JpaRepository<LobbyAndUser, Long> {

    @Query("SELECT COUNT(lu) FROM LobbyAndUser lu WHERE lu.lobbyId =?1")
    int countByLobbyId(Long lobbyId);
}
