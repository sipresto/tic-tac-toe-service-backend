package be.kdg.ipj3.application;

import be.kdg.ipj3.api.dto.AiGameStateDto;
import be.kdg.ipj3.api.dto.AiResponseDto;
import be.kdg.ipj3.domain.*;
import be.kdg.ipj3.infrastructure.ai.AiClient;
import be.kdg.ipj3.infrastructure.jpa.GameRepository;
import be.kdg.ipj3.infrastructure.messaging.MessageSendHandler;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final AiClient aiClient;
    private final Random random = new Random();
    private final MessageSendHandler messageSendHandler;

    public GameService(GameRepository gameRepository, AiClient aiClient, MessageSendHandler messageSendHandler) {
        this.gameRepository = gameRepository;
        this.aiClient = aiClient;
        this.messageSendHandler = messageSendHandler;
    }

    @Transactional
    public Game createGame(Difficulty difficulty, List<UUID> players) {
        Player px = Player.human(players.getFirst());
        Player po = (players.size() == 1) ? Player.ai() : Player.human(players.getLast());

        Game game = new Game(difficulty, px, po);
        gameRepository.save(game);
        return game;
    }

    public Game getGame(UUID gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new IllegalArgumentException("Game not found");
        }
        return game;
    }

    @Transactional
    public Game applyMove(UUID gameId, UUID playerId, int row, int col) {
        try {
            Game game = getGame(gameId);

            game.move(playerId, row, col);

            if (game.getStatus() != GameStatus.IN_PROGRESS) {
                removeGame(gameId);
            }

            if (game.getStatus() == GameStatus.FINISHED || game.getStatus() == GameStatus.DRAW) {
                messageSendHandler.sendGameResult(game);
                messageSendHandler.sendAchievementCompleted(game);
            }

            gameRepository.save(game);
            return game;


        } catch (ObjectOptimisticLockingFailureException e) {
            throw new IllegalStateException("Another move was applied simultaneously. Please reload the game.", e);
        }
    }

    @Transactional
    public Game applyAiMove(UUID gameId) {
        try {
            Game game = getGame(gameId);
            Player currentPlayer = game.getCurrentPlayer();

            if (currentPlayer.isHuman()) {
                throw new IllegalStateException("Current player is not AI");
            }

            int[] aiMove = getAiMoveWithFallback(game);
            game.move(currentPlayer.id(), aiMove[0], aiMove[1]);

            if (game.getStatus() == GameStatus.FINISHED || game.getStatus() == GameStatus.DRAW) {
                messageSendHandler.sendGameResult(game);
                messageSendHandler.sendAchievementCompleted(game);
            }

            if (game.getStatus() != GameStatus.IN_PROGRESS) {
                removeGame(gameId);
            }

            gameRepository.save(game);
            return game;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new IllegalStateException("Another move was applied simultaneously. Please reload the game.", e);
        }
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

    @Transactional
    public Game createRematch(UUID oldGameId) {
        try {
            Game oldGame = getGame(oldGameId);

            if (oldGame.getStatus() == GameStatus.IN_PROGRESS) {
                throw new IllegalStateException("Game is not finished yet");
            }

            Game newGame = new Game(
                    oldGame.getDifficulty(),
                    oldGame.getPlayerO(),
                    oldGame.getPlayerX()
            );

            gameRepository.save(newGame);

            removeGame(oldGameId);

            return newGame;

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new IllegalStateException("Another action was applied to the game simultaneously. Please reload.", e);
        }
    }

    private void removeGame(UUID gameId) {
        gameRepository.deleteById(gameId);
    }

    @Transactional
    public Game resign(UUID gameId, UUID playerId) {
        try {
            Game game = getGame(gameId);

            if (game.getStatus() != GameStatus.IN_PROGRESS) {
                throw new IllegalStateException("Game is not in progress");
            }

            Player resigningPlayer = game.getPlayerX().id().equals(playerId)
                    ? game.getPlayerX()
                    : game.getPlayerO();

            Player winner = resigningPlayer.equals(game.getPlayerX())
                    ? game.getPlayerO()
                    : game.getPlayerX();

            game.resign(winner);
            gameRepository.save(game);

            messageSendHandler.sendGameResult(game);
            messageSendHandler.sendAchievementCompleted(game);

            removeGame(gameId);


            return game;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new IllegalStateException("Another action was applied to the game simultaneously. Please reload.", e);
        }
    }
}
