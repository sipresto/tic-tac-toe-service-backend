package be.kdg.ipj3.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ResignRequestDto(@NotNull UUID playerId) {
}
