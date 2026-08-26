package moly.backend.domain.expedition.domain.repository;

import moly.backend.domain.expedition.domain.UserExpedition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExpeditionRepository extends JpaRepository<UserExpedition, Long> {
}
