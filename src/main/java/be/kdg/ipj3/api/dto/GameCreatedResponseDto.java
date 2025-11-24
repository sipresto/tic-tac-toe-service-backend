package be.kdg.ipj3.api.dto;

import java.util.UUID;

public record GameCreatedResponseDto(
        UUID gameId,
        String gameUrl,
        GameStateDto initialState
) {}
