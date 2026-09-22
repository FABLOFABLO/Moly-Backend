package moly.backend.domain.expedition.domain.repository;

import moly.backend.domain.expedition.domain.UserExpedition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserExpeditionRepository extends JpaRepository<UserExpedition, Long> {
    List<UserExpedition> findAllByExpedition_Id(Long expeditionId);

    Optional<UserExpedition> findByExpedition_IdAndUser_Id(Long expeditionId, Long userId);
}
