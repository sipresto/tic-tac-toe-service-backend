package be.kdg.ipj3.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record MoveDto(
        @Min(0) @Max(2) int row,
        @Min(0) @Max(2) int col
) {
}
