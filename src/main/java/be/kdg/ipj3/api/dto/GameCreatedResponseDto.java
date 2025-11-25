package be.kdg.ipj3.api.dto;

import jakarta.validation.constraints.NotBlank;

public record GameCreatedResponseDto(
        @NotBlank String gameUrl
) {
}
