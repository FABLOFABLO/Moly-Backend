package moly.backend.domain.expedition.domain.repository;

import moly.backend.domain.expedition.domain.Expedition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpeditionRepository extends JpaRepository<Expedition, Long> {
}
