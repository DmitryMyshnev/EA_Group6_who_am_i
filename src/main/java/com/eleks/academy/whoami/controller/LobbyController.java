package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.db.dto.CreateLobbyCommandDto;
import com.eleks.academy.whoami.db.dto.LobbyDto;
import com.eleks.academy.whoami.db.dto.LobbyFilter;
import com.eleks.academy.whoami.db.dto.LobbyUserDto;
import com.eleks.academy.whoami.db.dto.LobbyWithCountUsers;
import com.eleks.academy.whoami.db.dto.ThemeDto;
import com.eleks.academy.whoami.db.mapper.LobbyMapper;
import com.eleks.academy.whoami.db.mapper.ThemeMapper;
import com.eleks.academy.whoami.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lobbies")
public class LobbyController {

    private final LobbyService lobbyService;
    private final LobbyMapper lobbyMapper;
    private final ThemeMapper themeMapper;

    @PostMapping
    @Transactional
    public ResponseEntity<LobbyDto> createLobby(@RequestBody @Valid CreateLobbyCommandDto commandDto) {
        var lobby = lobbyService.createLobby(lobbyMapper.toModel(commandDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(lobbyMapper.toDto(lobby));
    }

    @GetMapping("/themes")
    @Transactional
    public ResponseEntity<List<ThemeDto>> findAllThemes() {
        return lobbyService.findAllThemes()
                .stream()
                .map(themeMapper::toDto)
                .collect(collectingAndThen(toList(), ResponseEntity::ok));
    }

    @GetMapping("/{id}/users")
    @Transactional
    public ResponseEntity<List<LobbyUserDto>> finAllUsersInLobby(@PathVariable Long id) {
        return lobbyService.findAllUsersByLobbyId(id)
                .stream()
                .map(lobbyMapper::toDto)
                .collect(collectingAndThen(toList(), ResponseEntity::ok));
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<LobbyWithCountUsers>> findAllLobbies() {
        var lobbies = lobbyService.findAllLobbies()
                .stream()
                .map(lobbyMapper::toDtoWithCountUser)
                .toList();
        this.map(lobbyService.findAllLobbyIdsWithJoinUser(), lobbies);
        return ResponseEntity.ok(lobbies);
    }

    @GetMapping("/filter")
    @Transactional
    public ResponseEntity<List<LobbyWithCountUsers>> lobbyFilter(@RequestBody LobbyFilter lobbyFilter) {
        var lobbies = lobbyService.filter(lobbyFilter)
                .stream()
                .map(lobbyMapper::toDtoWithCountUser)
                .toList();
        var ids = lobbies
                .stream()
                .map(LobbyWithCountUsers::getId)
                .toList();
        this.map(lobbyService.findAllLobbyIdsWithJoinUserIn(ids), lobbies);
        return ResponseEntity.ok(lobbies);
    }

    private void map(Stream<Long> source, List<LobbyWithCountUsers> target) {
        source.collect(Collectors.toMap(k -> k, v -> 1, Integer::sum))
                .forEach((k, v) -> target
                        .stream()
                        .filter(lobby -> lobby.getId().equals(k))
                        .findFirst()
                        .ifPresent(lobby -> lobby.setJoinPlayers(v)));
    }
}
