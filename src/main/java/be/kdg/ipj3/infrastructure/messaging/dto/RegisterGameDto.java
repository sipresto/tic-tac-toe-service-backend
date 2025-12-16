package be.kdg.ipj3.infrastructure.messaging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RegisterGameDto(
        @NotNull UUID id,
        @NotBlank String name,
        String description,
        BigDecimal price,
        @NotBlank String frontendUrl,
        @Size(max = 2000) String rules,
        List<RegisterAchievementDto> achievements,
        List<String> categories,
        String imageUrl,
        @NotNull Instant timestamp
) {
    private static final UUID GAME_ID = UUID.fromString("4475bbce-f608-47c2-978b-4492389f2f12");
    private static final String NAME = "Tic Tac Toe";
    private static final String DESCRIPTION = "Tic Tac Toe is a puzzle game for two players, \"X\" and \"O\", who take turns marking spaces in a 3×3 grid. The player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row wins the game.";
    private static final BigDecimal PRICE = BigDecimal.valueOf(1.99);
    // TODO: url aanpassen na nginx rework
    private static final String FRONTEND_URL = "/games/tictactoe";
    private static final String RULES = """
            Two players take turns.
            Players alternate marking empty spaces in the grid with their assigned mark (X or O).
            The first player to get three of their marks in a row (horizontally, vertically, or diagonally) wins the game.
            If all nine spaces are filled and no player has three in a row, the game ends in a tie.""";
    private static final List<String> CATEGORIES = List.of("NATIVE", "STRATEGY");

    private static final List<RegisterAchievementDto> ACHIEVEMENTS = List.of(
            new RegisterAchievementDto(
                    "Tic",
                    "TIC",
                    "Win your first game",
                    1,
                    10,
                    "INCREMENT",
                    "WIN_ANY"
            ),
            new RegisterAchievementDto(
                    "Tac",
                    "TAC",
                    "Win 5 games",
                    5,
                    20,
                    "INCREMENT",
                    "WIN_ANY"
            ),
            new RegisterAchievementDto(
                    "Toe",
                    "TOE",
                    "Win 10 games",
                    10,
                    30,
                    "INCREMENT",
                    "WIN_ANY"
            ),
            new RegisterAchievementDto(
                    "Getting warmed up",
                    "WARMED_UP",
                    "Play 10 games",
                    10,
                    15,
                    "INCREMENT",
                    "PLAY_ANY"
            ),
            new RegisterAchievementDto(
                    "Unbeatable",
                    "UNBEATABLE",
                    "Win or draw 10 games in a row",
                    1,
                    30,
                    "STREAK",
                    "WIN_OR_DRAW_ANY"
            ),
            new RegisterAchievementDto(
                    "Beat a bot",
                    "BEAT_BOT",
                    "Win a game against the AI",
                    1,
                    10,
                    "INCREMENT",
                    "WIN_AI"
            ),
            new RegisterAchievementDto(
                    "Pummel a player",
                    "PUMMEL_PLAYER",
                    "Win a game against another player",
                    1,
                    10,
                    "INCREMENT",
                    "WIN_PLAYER"
            ),
            new RegisterAchievementDto(
                    "X-celent",
                    "X_CELENT",
                    "Win a game while playing as X",
                    null,
                    10,
                    null,
                    null
            ),
            new RegisterAchievementDto(
                    "O-mazing",
                    "O_MAZING",
                    "Win a game while playing as O",
                    null,
                    10,
                    null,
                    null
            )
    );


    public static RegisterGameDto create() {
        return new RegisterGameDto(
                GAME_ID,
                NAME,
                DESCRIPTION,
                PRICE,
                FRONTEND_URL,
                RULES,
                ACHIEVEMENTS,
                CATEGORIES,
                null,
                Instant.now()
        );
    }
}
