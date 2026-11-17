package moly.backend.domain.expedition.domain.repository;

import jakarta.persistence.LockModeType;
import moly.backend.domain.expedition.domain.Expedition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ExpeditionRepository extends JpaRepository<Expedition, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select expedition from Expedition expedition where expedition.id = :expeditionId")
    Optional<Expedition> findByIdForUpdate(@Param("expeditionId") Long expeditionId);
}
