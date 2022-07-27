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
        lobbyService.findAllLobbyIdsWithJoinUser()
                .collect(Collectors.toMap(k -> k, v -> 1, Integer::sum))
                .forEach((k, v) -> lobbies
                        .stream()
                        .filter(lobby -> lobby.getId().equals(k))
                        .findFirst()
                        .ifPresent(lobby -> lobby.setJoinPlayers(v)));
        return ResponseEntity.ok(lobbies);
    }

    @GetMapping("/filter")
    @Transactional
    public ResponseEntity<List<LobbyWithCountUsers>> lobbyFilter(@RequestBody LobbyFilter lobbyFilter) {
        if (lobbyFilter.getThemeFilters() == null) {
            lobbyFilter.setThemeFilters(List.of());
        }
        return lobbyService.filter(lobbyFilter)
                .stream()
                .map(lobby -> {
                    var lb = lobbyMapper.toDtoWithCountUser(lobby);
                    lb.setJoinPlayers(lobbyService.countJoinPlayers(lobby.getId()));
                    return lb;
                })
                .collect(collectingAndThen(toList(), ResponseEntity::ok));
    }
}
