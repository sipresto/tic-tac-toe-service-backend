package be.kdg.ipj3.infrastructure.jpa;

import be.kdg.ipj3.domain.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface JpaGameRepository extends JpaRepository<JpaGameEntity, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM JpaGameEntity g WHERE g.status IN :statuses OR g.lastActivity < :cutoff")
    void deleteByStatusOrLastActivityBefore(
            @Param("statuses") List<GameStatus> statuses,
            @Param("cutoff") Instant cutoff
    );
}
