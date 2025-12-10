package be.kdg.ipj3.api.dto;

import be.kdg.ipj3.domain.Player;
import be.kdg.ipj3.domain.UserId;

public record SmallUserDto(
        UserId id,
        String name
) {
    public static SmallUserDto fromDomain(Player user) {
        return new SmallUserDto(user.id(), user.name());
    }
}

