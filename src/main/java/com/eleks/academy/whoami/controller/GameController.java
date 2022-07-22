package com.eleks.academy.whoami.controller;


import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.GuessMessage;
import com.eleks.academy.whoami.model.request.Message;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.eleks.academy.whoami.utils.StringUtils.Headers.PLAYER;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Validated
public class GameController {

    private final GameService gameService;

    @Operation(summary = "To start Quick-game. After first time request create new game and will add first player with name from header." +
            " After next request will add new player to game with name from  header." +
            " Return info about new game.")
    @GetMapping("/quick-game")
    public ResponseEntity<GameDetails> quickGame(@RequestHeader(PLAYER) String player,
                                                 @RequestBody NewGameRequest gameRequest) {
        return this.gameService.quickGame(player, gameRequest.getMaxPlayers())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "To create new game and to add first player with name from header. " +
            "Return info about new game")
    @PostMapping
    public ResponseEntity<GameDetails> createGame(@RequestHeader(PLAYER) String player,
                                                  @RequestBody NewGameRequest gameRequest) {
        return this.gameService.quickGame(player, gameRequest.getMaxPlayers())
                .map(gameDetails -> ResponseEntity.status(HttpStatus.CREATED).body(gameDetails))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Find all existing game and game where has player with name from header. " +
            " Return info about all games")
    @GetMapping
    public List<GameLight> findAvailableGames(@RequestHeader(PLAYER) String player) {
        return this.gameService.findAvailableGames(player);
    }

    @Operation(summary = "Add new Player with name from header to game")
    @PostMapping("/{id}/players")
    public ResponseEntity<SynchronousPlayer> enrollToGame(@PathVariable("id") String id,
                                                          @RequestHeader(PLAYER) String player) {
        return this.gameService.enrollToGame(id, player)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Add character to player with name from header")
    @PostMapping("/{id}/characters")
    @ResponseStatus(HttpStatus.OK)
    public void suggestCharacter(@PathVariable("id") String id,
                                 @RequestHeader(PLAYER) String player,
                                 @Valid @RequestBody CharacterSuggestion suggestion) {
        this.gameService.suggestCharacter(id, player, suggestion.getCharacter());
    }

    @Operation(summary = "Find game by ID. Name in header does not matter. Return info about game")
    @GetMapping("/{id}")
    public ResponseEntity<GameDetails> findById(@PathVariable("id") String id) {
        return this.gameService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Rename  player in game. Header - old name. Query - new name. Return player with new name")
    @PostMapping("/{id}/players/name")
    public ResponseEntity<SynchronousPlayer> renamePlayer(@PathVariable("id") String id,
                                                          @RequestHeader(PLAYER) String player,
                                                          @RequestParam(value = "name") @Length(min = 2, max = 50) String newName) {
        return this.gameService.renamePlayer(id, player, newName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Start game. Available if status game is READY_TO_START. Name in header does not matter ")
    @PostMapping("/{id}")
    public ResponseEntity<GameDetails> startGame(@PathVariable("id") String id,
                                                 @RequestHeader(PLAYER) String player) {
        return this.gameService.startGame(id, player)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = " To ask question by player with name from header. " +
            "Available if status game is PROCESSING_QUESTION and status player is ASKING")
    @PostMapping("/{id}/questions")
    @ResponseStatus(HttpStatus.OK)
    public void askQuestion(@PathVariable("id") String id,
                            @RequestHeader(PLAYER) String player,
                            @Valid @RequestBody Message message) {

        this.gameService.askQuestion(id, player, message.getMessage());
    }

    @Operation(summary = "Leave player from game. Return info about game")
    @GetMapping("/{id}/leave-game")
    public ResponseEntity<GameDetails> leaveGame(@RequestHeader(PLAYER) String player, @PathVariable("id") String id) {
        return this.gameService.leaveGame(player, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Answer by player on question or guessing. " +
            "Available if status game is PROCESSING_QUESTION and status player is ANSWERING")
    @PostMapping("/{id}/answer")
    @ResponseStatus(HttpStatus.OK)
    public void answerQuestion(@PathVariable("id") String id,
                               @RequestHeader(PLAYER) String player,
                               @RequestBody Message message) {
        this.gameService.answerQuestion(id, player, message.getMessage());
    }

    @Operation(summary = "Guess character by player. " +
            "Available if status game is PROCESSING_QUESTION and status player is ASKING")
    @PostMapping("/{id}/guess")
    @ResponseStatus(HttpStatus.OK)
    public void guessingCharacter(@PathVariable("id") String id,
                                  @RequestHeader(PLAYER) String player,
                                  @RequestBody GuessMessage message) {
        this.gameService.guessingCharacter(id, player, message.getMessage());
    }
}