package be.kdg.ipj3.domain;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID id) {
    public UserId {
        Objects.requireNonNull(id, "User id cannot be null");
    }

    public static UserId create() {
        return new UserId(UUID.randomUUID());
    }
}

