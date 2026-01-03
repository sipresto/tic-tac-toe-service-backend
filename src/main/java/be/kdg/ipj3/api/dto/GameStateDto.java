package be.kdg.ipj3.api.dto;

import be.kdg.ipj3.domain.CellValue;
import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.domain.Player;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GameStateDto(
        @NotNull UUID id,
        @NotNull CellValue[][] board,
        @NotNull PlayerDto playerX,
        @NotNull PlayerDto playerO,
        @NotNull boolean playerXWantsRematch,
        @NotNull boolean playerOWantsRematch,
        @NotNull String currentPlayer,
        @NotNull String status,
        String winner,
        UUID newId,
        @NotNull boolean leftGame
) {
    public static GameStateDto fromDomain(Game game) {
        return new GameStateDto(
                game.getId(),
                game.getBoard(),
                PlayerDto.fromDomain(game.getPlayerX()),
                PlayerDto.fromDomain(game.getPlayerO()),
                game.isPlayerXWantsRematch(),
                game.isPlayerOWantsRematch(),
                game.getCurrentPlayer().id().id(),
                game.getStatus().name(),
                game.getWinner() != null ? game.getWinner().id().id() : null,
                game.getNewId(),
                game.hasLeftGame()
        );
    }

    public record PlayerDto(
            @NotNull String id,
            @NotNull boolean isHuman,
            @NotNull String name
    ) {
        public static PlayerDto fromDomain(Player player) {
            return new PlayerDto(player.id().id(), player.isHuman(), player.name());
        }
    }
}
