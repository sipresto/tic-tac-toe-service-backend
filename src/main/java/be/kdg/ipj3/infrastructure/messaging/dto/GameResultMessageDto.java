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
                                   String finalStatus,
                                   Instant completedAt
) {

    public static GameResultMessageDto fromDomain(Game game) {
        return new GameResultMessageDto(
                game.getId(),
                game.getPlayerX().id(),
                game.getPlayerO().id(),
                game.getWinner() != null ? game.getWinner().id() : null,
                game.getDifficulty(),
                game.getStatus().name(),
                game.getLastActivity()
        );
    }
}

