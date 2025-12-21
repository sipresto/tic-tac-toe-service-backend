package be.kdg.ipj3.infrastructure.messaging.dto;

import java.util.UUID;

public record GameRematchDto(
        UUID oldGameId,
        UUID newGameId
) {
}
