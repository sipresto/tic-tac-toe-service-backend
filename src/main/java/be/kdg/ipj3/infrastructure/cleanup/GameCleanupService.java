package be.kdg.ipj3.infrastructure.cleanup;

import be.kdg.ipj3.domain.GameStatus;
import be.kdg.ipj3.infrastructure.jpa.JpaGameRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class GameCleanupService {
    private final JpaGameRepository jpaGameRepository;

    public GameCleanupService(JpaGameRepository jpaGameRepository) {
        this.jpaGameRepository = jpaGameRepository;
    }

    @Scheduled(fixedRate = 600000)
    public void cleanupStaleGames() {
        Instant cutoffTime = Instant.now().minus(Duration.ofMinutes(30));

        jpaGameRepository.deleteByStatusOrLastActivityBefore(
                List.of(GameStatus.FINISHED, GameStatus.DRAW),
                cutoffTime
        );
    }
}
