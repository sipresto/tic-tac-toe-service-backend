package be.kdg.ipj3.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PlatformPlayerDto(
        @NotNull UUID id,
        @NotNull String name
) {
    public static PlatformPlayerDto fromDto(SmallUserDto smallUserDto) {
        return new PlatformPlayerDto(smallUserDto.id().id(),  smallUserDto.name());
    }
}
