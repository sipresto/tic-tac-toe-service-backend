package be.kdg.ipj3.api.dto;

import be.kdg.ipj3.domain.Difficulty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record GameStartRequestDto(
        @NotNull UUID gameId,
        @Valid @Size(min = 1) List<PlatformPlayerDto> players,
        Difficulty difficulty
) {
    public record PlatformPlayerDto(
            @NotBlank String keycloakId,
            @NotNull String displayName
    ) {
    }
}
