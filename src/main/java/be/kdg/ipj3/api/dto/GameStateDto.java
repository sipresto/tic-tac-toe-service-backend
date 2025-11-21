package be.kdg.ipj3.api.dto;

import be.kdg.ipj3.domain.CellValue;
import be.kdg.ipj3.domain.Game;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GameStateDto(
        @NotNull UUID id,
        @NotNull CellValue[][] board,
        @NotNull UUID currentPlayer,
        @NotNull String status,
        UUID winner
) {
    public static GameStateDto fromDomain(Game game) {
        return new GameStateDto(
                game.getId(),
                game.getBoard(),
                game.getCurrentPlayer().id(),
                game.getStatus().name(),
                game.getWinner() != null ? game.getWinner().id() : null
        );
    }
}
