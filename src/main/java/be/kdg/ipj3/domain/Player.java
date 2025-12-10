package be.kdg.ipj3.domain;

import java.util.UUID;

public record Player(
        UserId id,
        boolean isHuman,
        String name
) {
    public static Player human(UUID id) {
        return new Player(new UserId(id), true, "human");
    }

    public static Player ai() {
        return new Player(new UserId(UUID.randomUUID()), false, "ai");
    }
}
