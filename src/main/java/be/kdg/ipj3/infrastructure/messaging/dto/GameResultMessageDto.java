package be.kdg.ipj3.infrastructure.messaging.dto;

import be.kdg.ipj3.domain.Difficulty;
import be.kdg.ipj3.domain.Game;

import java.time.Instant;
import java.util.UUID;

public record GameResultMessageDto(UUID gameId,
                                   UUID playerXId,
                                   UUID playerOId,
                                   UUID winnerId,
                                   Difficulty difficulty,
                                   SessionResult finalStatus,
                                   Instant completedAt
) {

    public static GameResultMessageDto fromDomain(Game game) {
        return new GameResultMessageDto(
                game.getId(),
                game.getPlayerX().isHuman() ? game.getPlayerX().id() : null,
                game.getPlayerO().isHuman() ? game.getPlayerO().id() : null,
                game.getWinner() != null && game.getWinner().isHuman()
                        ? game.getWinner().id()
                        : null,
                game.getDifficulty(),
                game.getWinner() == null
                        ? SessionResult.DRAW
                        : (game.getWinner().isHuman()
                        ? SessionResult.PLAYER_WIN
                        : SessionResult.AI_WIN),
                game.getLastActivity()
        );
    }
}

