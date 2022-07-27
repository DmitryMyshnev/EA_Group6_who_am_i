package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.db.model.Lobby;
import com.eleks.academy.whoami.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    @Query(value = "SELECT  u FROM Lobby l JOIN LobbyAndUser lu ON(lu.lobbyId = l.id) JOIN User u ON(u.id = lu.userId) WHERE l.id =?1")
    List<User> findAllUsersByLobbyId(Long lobbyId);
}
