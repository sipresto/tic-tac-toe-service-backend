package be.kdg.ipj3.api;

import be.kdg.ipj3.api.dto.GameStartDto;
import be.kdg.ipj3.api.dto.GameStateDto;
import be.kdg.ipj3.api.dto.MoveDto;
import be.kdg.ipj3.application.GameService;
import be.kdg.ipj3.domain.Difficulty;
import be.kdg.ipj3.domain.Game;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/tictactoe")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameStateDto> createGame(
            @Valid @RequestBody GameStartDto dto
    ) {
        Game game = gameService.createGame(Difficulty.fromString(dto.difficulty()), dto.players());
        GameStateDto state = GameStateDto.fromDomain(game);
        URI location = URI.create("/api/tictactoe/" + game.getId());
        return ResponseEntity.created(location).body(state);
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

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> abandonGame(
            @PathVariable UUID gameId
    ) {
        gameService.removeGame(gameId);
        return ResponseEntity.noContent().build();
    }
}
