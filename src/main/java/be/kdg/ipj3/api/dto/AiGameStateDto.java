package be.kdg.ipj3.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AiGameStateDto(
        @JsonProperty("game_name")
        String gameName,

        @JsonProperty("difficulty")
        String difficulty,

        @JsonProperty("current_player")
        String currentPlayer,

        @JsonProperty("board_state")
        List<List<String>> boardState
) {
}
