package be.kdg.ipj3.domain;

import java.util.UUID;

public record Player(
        UUID id,
        boolean isHuman
) {
    public static Player human(UUID id) {
        return new Player(id, true);
    }

    public static Player ai() {
        return new Player(UUID.randomUUID(), false);
    }
}
