package be.kdg.ipj3.application;

import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.domain.GameStatus;
import be.kdg.ipj3.domain.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<UUID, Game> games = new ConcurrentHashMap<>();

    public Game createGame(UUID playerIdX, UUID playerIdO) {
        boolean xIsHuman = playerIdX != null;
        boolean oIsHuman = playerIdO != null;
        Game game = new Game(new Player(playerIdX, xIsHuman), new Player(playerIdO, oIsHuman));
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

        if (!game.isCurrentPlayer(playerId)) {
            throw new IllegalStateException("Not your turn");
        }

        game.move(row, col);

        if (game.getStatus() == GameStatus.DRAW) {
            games.remove(gameId);
        } else if (game.getStatus() == GameStatus.FINISHED) {

        }

        return game;
    }

    public void removeGame(UUID gameId) {
        games.remove(gameId);
    }
}