package be.kdg.ipj3.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AiResponseDto(
        @JsonProperty("best_move")
        List<Integer> bestMove,

        @JsonProperty("updated_board")
        List<List<String>> updatedBoard
) {
}
