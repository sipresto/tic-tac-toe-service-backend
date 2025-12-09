package be.kdg.ipj3.api;

import be.kdg.ipj3.api.dto.*;
import be.kdg.ipj3.application.GameService;
import be.kdg.ipj3.domain.Game;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/tictactoe")
public class GameController {
    private final GameService gameService;

    @Value("${game.base-url}")
    private String gameBaseUrl;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameCreatedResponseDto> createGame(
            @Valid @RequestBody GameStartRequestDto dto
    ) {
        Game game = gameService.createGame(dto.difficulty(), dto.players());
        String gameUrl;
        if (!gameBaseUrl.contains("games")) {
            gameUrl = gameBaseUrl + "/games/tictactoe/" + game.getId();
        }
        else {
            gameUrl = gameBaseUrl + game.getId();
        }
        GameCreatedResponseDto response = new GameCreatedResponseDto(gameUrl);

        URI location = URI.create("/api/games/tictactoe/" + game.getId());

        return ResponseEntity.created(location).body(response);
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
        Game game = gameService.applyMove(gameId, dto.playerId(), dto.row(), dto.col());
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
