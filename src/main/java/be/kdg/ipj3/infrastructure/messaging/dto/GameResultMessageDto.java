package be.kdg.ipj3.infrastructure.messaging.dto;

import be.kdg.ipj3.domain.Difficulty;
import be.kdg.ipj3.domain.Game;

import java.time.Instant;
import java.util.*;

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
        List<UUID> players = new ArrayList<>();
        Map<String, Map<String, Object>> metadata = new HashMap<>();

        if (game.getPlayerX().isHuman()) {
            players.add(game.getPlayerX().id().id());
            metadata.put(game.getPlayerX().id().toString(), Map.of("playerSide", "X"));
        }
        if (game.getPlayerO().isHuman()) {
            players.add(game.getPlayerO().id().id());
            metadata.put(game.getPlayerO().id().toString(), Map.of("playerSide", "O"));
        }

        // TODO: this is the gameid of TTT in data.sql, shouldn't be used like this
        return new GameResultMessageDto(
                UUID.fromString("c3c1d2b0-82fa-43a7-b326-0eb96136ecbc"),
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
        );
    }

    public enum SessionResult {
        PLAYER_WIN,
        AI_WIN,
        DRAW
    }
}

