package be.kdg.ipj3.domain;

import java.util.Objects;

public record UserId(String id) {
    public UserId {
        Objects.requireNonNull(id, "User id cannot be null");
    }
}
