package be.kdg.ipj3.infrastructure.jpa;

import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.domain.IGameRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class GameRepository implements IGameRepository {
    private final JpaGameRepository jpaGameRepository;

    public GameRepository(JpaGameRepository jpaGameRepository) {
        this.jpaGameRepository = jpaGameRepository;
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return this.jpaGameRepository.findById(id)
                .map(JpaGameEntity::toDomain);
    }

    @Override
    public void save(Game game) {
        // Let Spring Data JPA handle the save operation
        // It will automatically detect if it's an insert or update
        JpaGameEntity jpaGameEntity = JpaGameEntity.fromDomain(game);
        this.jpaGameRepository.save(jpaGameEntity);
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaGameRepository.deleteById(id);
    }
}