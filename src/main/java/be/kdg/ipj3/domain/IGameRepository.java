package be.kdg.ipj3.domain;

import java.util.Optional;
import java.util.UUID;

public interface IGameRepository {
    Optional<Game> findById(UUID id);

    void save(Game game);

    void deleteById(UUID id);
}
