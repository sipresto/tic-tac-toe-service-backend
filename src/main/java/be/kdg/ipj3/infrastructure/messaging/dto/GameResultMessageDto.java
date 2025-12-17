package be.kdg.ipj3.infrastructure.messaging.dto;

import be.kdg.ipj3.domain.Difficulty;
import be.kdg.ipj3.domain.Game;

import java.time.Instant;
import java.util.*;

import static be.kdg.ipj3.infrastructure.config.GameInfo.GAME_ID;

public record GameResultMessageDto(
        UUID gameId,
        List<UUID> players,
        UUID winnerId,
        Difficulty difficulty,
        SessionResult sessionResult,
        Instant completedAt,
        Map<String, Map<String, Object>> metadata
) {

    public static GameResultMessageDto fromDomain(Game game) {
        /*List<UUID> players = new ArrayList<>();
        Map<String, Map<String, Object>> metadata = new HashMap<>();

        if (game.getPlayerX().isHuman()) {
            players.add(game.getPlayerX().id().id());
            metadata.put(game.getPlayerX().id().toString(), Map.of("playerSide", "X"));
        }
        if (game.getPlayerO().isHuman()) {
            players.add(game.getPlayerO().id().id());
            metadata.put(game.getPlayerO().id().toString(), Map.of("playerSide", "O"));
        }

        return new GameResultMessageDto(
                GAME_ID,
                players,
                game.getWinner() != null && game.getWinner().isHuman()
                        ? game.getWinner().id().id()
                        : null,
                game.getDifficulty(),
                game.getWinner() == null
                        ? SessionResult.DRAW
                        : (game.getWinner().isHuman()
                        ? SessionResult.PLAYER_WIN
                        : SessionResult.AI_WIN),
                game.getLastActivity(),
                metadata
        );*/
        return null;
    }

    public enum SessionResult {
        PLAYER_WIN,
        AI_WIN,
        DRAW
    }
}

