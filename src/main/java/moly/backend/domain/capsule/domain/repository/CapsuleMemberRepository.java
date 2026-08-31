package moly.backend.domain.capsule.domain.repository;

import moly.backend.domain.capsule.domain.CapsuleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CapsuleMemberRepository extends JpaRepository<CapsuleMember, Long> {
    Optional<CapsuleMember> findByCapsule_IdAndUser_Id(Long capsuleId, Long userId);

    boolean existsByCapsule_IdAndUser_Id(Long capsuleId, Long userId);

    List<CapsuleMember> findAllByCapsule_Id(Long capsuleId);

    @Query("select distinct member.user.id from CapsuleMember member where member.user.id in :userIds "
            + "and member.capsule.expedition is null and member.capsule.createdAt >= :start "
            + "and member.capsule.createdAt < :end")
    List<Long> findPersonalCapsuleUserIdsCreatedBetween(
            @Param("userIds") Collection<Long> userIds,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Modifying
    @Query("delete from CapsuleMember member where member.capsule.id = :capsuleId")
    void deleteAllByCapsuleId(@Param("capsuleId") Long capsuleId);
}
