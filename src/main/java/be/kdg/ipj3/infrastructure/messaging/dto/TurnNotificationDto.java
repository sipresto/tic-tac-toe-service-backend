package be.kdg.ipj3.infrastructure.messaging.dto;

import java.util.UUID;

public record TurnNotificationDto(
        UUID userId,
        String description
) {
}
