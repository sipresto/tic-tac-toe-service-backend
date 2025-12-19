package be.kdg.ipj3.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterAchievementDto(
        @NotBlank String name,
        @NotBlank String code,
        @NotBlank String description,
        Integer requiredValue,
        Integer points,
        String countingStrategy,
        String countTrigger
) {
}