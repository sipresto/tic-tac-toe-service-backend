package be.kdg.ipj3.infrastructure.cleanup;

import be.kdg.ipj3.domain.Game;
import be.kdg.ipj3.domain.GameStatus;
import be.kdg.ipj3.domain.Player;
import be.kdg.ipj3.infrastructure.jpa.JpaGameEntity;
import be.kdg.ipj3.infrastructure.jpa.JpaGameRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class GameCleanupService {
    private final JpaGameRepository jpaGameRepository;

    @Value("${finished.cutoff.minutes}")
    private int finishedGameCutoffMinutes;

    @Value("${abandoned.cutoff.minutes}")
    private int abandonedGameCutoffMinutes;

    public GameCleanupService(JpaGameRepository jpaGameRepository) {
        this.jpaGameRepository = jpaGameRepository;
    }

    @Scheduled(fixedRate = 600000)
    @Transactional
    public void cleanupStaleGames() {
        Instant abandonmentCutoff = Instant.now().minus(Duration.ofMinutes(abandonedGameCutoffMinutes));
        Instant finishedGameCutoff = Instant.now().minus(Duration.ofMinutes(finishedGameCutoffMinutes));

        List<JpaGameEntity> abandonedGames = jpaGameRepository
                .findByStatusAndLastActivityBefore(GameStatus.IN_PROGRESS, abandonmentCutoff);

        for (JpaGameEntity entity : abandonedGames) {
            Game game = entity.toDomain();

            Player winner = game.getCurrentPlayer().equals(game.getPlayerX())
                    ? game.getPlayerO()
                    : game.getPlayerX();

            game.resign(winner);

            jpaGameRepository.save(JpaGameEntity.fromDomain(game));
        }

        jpaGameRepository.deleteByStatusAndLastActivityBefore(
                List.of(GameStatus.FINISHED, GameStatus.DRAW),
                finishedGameCutoff
        );
    }
}
