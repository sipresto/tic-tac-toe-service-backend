package be.kdg.ipj3.infrastructure.messaging.dto;

public record TurnNotificationDto(
        String userId,
        String description
) {
}
