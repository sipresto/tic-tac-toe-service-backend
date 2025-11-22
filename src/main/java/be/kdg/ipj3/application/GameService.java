package be.kdg.ipj3.application;

import be.kdg.ipj3.api.dto.AiGameStateDto;
import be.kdg.ipj3.api.dto.AiResponseDto;
import be.kdg.ipj3.domain.CellValue;
import be.kdg.ipj3.domain.Difficulty;
import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.domain.Player;
import be.kdg.ipj3.infrastructure.AiClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final AiClient aiClient;

    private final Map<UUID, Game> games = new ConcurrentHashMap<>();

    public GameService(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    public Game createGame(Difficulty difficulty, List<UUID> players) {
        Player px = Player.human(players.getFirst());
        Player po = (players.size() == 1) ? Player.ai() : Player.human(players.getLast());

        Game game = new Game(difficulty, px, po);
        games.put(game.getId(), game);
        return game;
    }

    public Game getGame(UUID gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found");
        }
        return game;
    }

    public Game applyMove(UUID gameId, UUID playerId, int row, int col) {
        Game game = getGame(gameId);
        game.move(playerId, row, col);
        Player nextPlayer = game.getCurrentPlayer();

        if (!nextPlayer.isHuman()) {
            AiGameStateDto aiDto = buildAiStateDto(game);
            AiResponseDto aiResponse = aiClient.getAiMove(aiDto);

            int aiRow = aiResponse.bestMove().get(0);
            int aiCol = aiResponse.bestMove().get(1);

            game.move(nextPlayer.id(), aiRow, aiCol);
        }

        return game;
    }

    public AiGameStateDto buildAiStateDto(Game game) {
        return new AiGameStateDto(
                "tic_tac_toe",
                mapDifficulty(game.getDifficulty()),
                game.getCurrentPlayer() == game.getPlayerX() ? "X" : "O",
                convertBoard(game.getBoard())
        );
    }

    private List<List<String>> convertBoard(CellValue[][] board) {
        return Arrays.stream(board)
                .map(row -> Arrays.stream(row)
                        .map(cell -> cell == CellValue.EMPTY ? null : cell.name())
                        .toList())
                .toList();
    }

    public Game createRematch(UUID oldGameId) {
        Game oldGame = getGame(oldGameId);

        Game newGame = new Game(
                oldGame.getDifficulty(),
                oldGame.getPlayerO(),
                oldGame.getPlayerX()
        );

        games.put(newGame.getId(), newGame);

        games.remove(oldGame.getId());

        return newGame;
    }

    private String mapDifficulty(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> "easy";
            case HARD -> "hard";
            case IMPOSSIBLE -> "impossible";
        };
    }
}