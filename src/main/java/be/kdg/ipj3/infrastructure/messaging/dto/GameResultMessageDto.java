package be.kdg.ipj3.infrastructure.messaging.dto;

import be.kdg.ipj3.domain.Difficulty;
import be.kdg.ipj3.domain.Game;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record GameResultMessageDto(
        UUID gameId,
        List<String> players,
        String winnerId,
        Difficulty difficulty,
        String status,
        Instant completedAt
) {

    public static GameResultMessageDto fromDomain(Game game) {
        List<String> players = new ArrayList<>();

        if (game.getPlayerX().isHuman()) {
            players.add(game.getPlayerX().id().id());
        }
        if (game.getPlayerO().isHuman()) {
            players.add(game.getPlayerO().id().id());
        }

        return new GameResultMessageDto(
                game.getId(),
                players,
                game.getWinner() != null && game.getWinner().isHuman()
                        ? game.getWinner().id().id()
                        : null,
                game.getDifficulty(),
                game.getWinner() == null
                        ? "DRAW"
                        : (game.getWinner().isHuman()
                        ? "PLAYER_WIN"
                        : "AI_WIN"),
                game.getLastActivity()
        );
    }
}

