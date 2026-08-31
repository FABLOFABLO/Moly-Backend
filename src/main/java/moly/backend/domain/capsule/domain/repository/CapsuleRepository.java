package moly.backend.domain.capsule.domain.repository;

import jakarta.persistence.LockModeType;
import moly.backend.domain.capsule.domain.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select capsule from Capsule capsule where capsule.id = :capsuleId")
    Optional<Capsule> findByIdForUpdate(@Param("capsuleId") Long capsuleId);

    @Query("select capsule from Capsule capsule join CapsuleMember member on member.capsule = capsule "
            + "where member.user.id = :userId and capsule.openedTime is null order by capsule.createdAt desc")
    List<Capsule> findUnopenedByMemberUserId(@Param("userId") Long userId);

    @Query("select capsule from Capsule capsule join CapsuleMember member on member.capsule = capsule "
            + "where member.user.id = :userId and capsule.openedTime is not null order by capsule.openedTime desc")
    List<Capsule> findOpenedByMemberUserId(@Param("userId") Long userId);

    @Query("select count(capsule) from Capsule capsule where capsule.expedition.id = :expeditionId "
            + "and capsule.createdAt >= :start and capsule.createdAt < :end")
    long countExpeditionCapsulesCreatedBetween(
            @Param("expeditionId") Long expeditionId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = "select ST_DWithin(c.location::geography, "
            + "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, :distanceMeters) "
            + "from tbl_capsule c where c.capsule_id = :capsuleId", nativeQuery = true)
    boolean isWithinArrivalRange(
            @Param("capsuleId") Long capsuleId,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("distanceMeters") double distanceMeters
    );
}
