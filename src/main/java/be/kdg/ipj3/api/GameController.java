package be.kdg.ipj3.api;

import be.kdg.ipj3.api.dto.*;
import be.kdg.ipj3.application.GameService;
import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.domain.Player;
import be.kdg.ipj3.domain.UserId;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tictactoe")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Void> createGame(
            @Valid @RequestBody GameStartRequestDto dto
    ) {
        List<Player> players = dto.players().stream()
                .map(playerDto -> Player.human(
                        playerDto.keycloakId(),
                        playerDto.displayName()
                ))
                .toList();

        gameService.createGame(dto.gameId(), players, dto.difficulty());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateDto> getGame(
            @PathVariable UUID gameId
    ) {
        Game game = gameService.getGame(gameId);
        return ResponseEntity.ok(GameStateDto.fromDomain(game));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameStateDto> makeMove(
            @PathVariable UUID gameId,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody MoveDto dto
    ) {
        Game game = gameService.applyMove(gameId, jwt.getSubject(), dto.row(), dto.col());
        return ResponseEntity.ok(GameStateDto.fromDomain(game));
    }

    @PostMapping("/{gameId}/ai-move")
    public ResponseEntity<GameStateDto> makeAiMove(
            @PathVariable UUID gameId
    ) {
        Game game = gameService.applyAiMove(gameId);
        return ResponseEntity.ok(GameStateDto.fromDomain(game));
    }

    @PostMapping("/{gameId}/rematch")
    public ResponseEntity<GameStateDto> rematch(
            @PathVariable UUID gameId
    ) {
        Game newGame = gameService.createRematch(gameId);
        GameStateDto state = GameStateDto.fromDomain(newGame);
        URI location = URI.create("/api/tictactoe/" + newGame.getId());
        return ResponseEntity.created(location).body(state);
    }

    @PostMapping("/{gameId}/resign")
    public ResponseEntity<GameStateDto> resign(
            @PathVariable UUID gameId,
            @Valid @RequestBody ResignRequestDto dto
    ) {
        Game game = gameService.resign(gameId, dto.playerId());
        return ResponseEntity.ok(GameStateDto.fromDomain(game));
    }
}
