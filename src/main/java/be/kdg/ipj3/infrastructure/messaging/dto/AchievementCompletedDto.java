package be.kdg.ipj3.infrastructure.messaging.dto;

import java.time.Instant;
import java.util.UUID;

public record AchievementCompletedDto(
        String userId,
        UUID gameId,
        String code,
        Instant timestamp
) {
}