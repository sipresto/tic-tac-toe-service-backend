package be.kdg.ipj3.application;

import be.kdg.ipj3.api.dto.AiGameStateDto;
import be.kdg.ipj3.api.dto.AiResponseDto;
import be.kdg.ipj3.domain.CellValue;
import be.kdg.ipj3.domain.Difficulty;
import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.domain.Player;
import be.kdg.ipj3.infrastructure.AiClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final AiClient aiClient;
    private final Random random = new Random();

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
        return game;
    }

    public Game applyAiMove(UUID gameId) {
        Game game = getGame(gameId);
        Player currentPlayer = game.getCurrentPlayer();

        if (currentPlayer.isHuman()) {
            throw new IllegalStateException("Current player is not AI");
        }

        int[] aiMove = getAiMoveWithFallback(game);
        game.move(currentPlayer.id(), aiMove[0], aiMove[1]);

        return game;
    }

    private int[] getAiMoveWithFallback(Game game) {
        try {
            AiGameStateDto aiDto = buildAiStateDto(game);
            AiResponseDto aiResponse = aiClient.getAiMove(aiDto);

            int aiRow = aiResponse.bestMove().get(0);
            int aiCol = aiResponse.bestMove().get(1);

            return new int[]{aiRow, aiCol};
        } catch (Exception e) {
            return getRandomMove(game.getBoard());
        }
    }

    private int[] getRandomMove(CellValue[][] board) {
        List<int[]> availableMoves = new ArrayList<>();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == CellValue.EMPTY) {
                    availableMoves.add(new int[]{row, col});
                }
            }
        }

        if (availableMoves.isEmpty()) {
            throw new IllegalStateException("No available moves");
        }

        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    public AiGameStateDto buildAiStateDto(Game game) {
        return new AiGameStateDto(
                "tic_tac_toe",
                game.getDifficulty().toString(),
                game.getCurrentPlayer().equals(game.getPlayerX()) ? "X" : "O",
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
}