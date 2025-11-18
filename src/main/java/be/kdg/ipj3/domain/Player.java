package be.kdg.ipj3.domain;

import java.util.UUID;

public class Player {
    private final UUID id;
    private final boolean isHuman;

    public Player(UUID id, boolean isHuman) {
        this.id = id;
        this.isHuman = isHuman;
    }

    public UUID getId() {
        return id;
    }

    public boolean isHuman() {
        return isHuman;
    }
}
