package moly.backend.domain.capsule.domain.repository;

import moly.backend.domain.capsule.domain.ArrivalStatus;
import moly.backend.domain.capsule.domain.CapsuleArrival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CapsuleArrivalRepository extends JpaRepository<CapsuleArrival, Long> {
    Optional<CapsuleArrival> findByCapsuleMember_Id(Long capsuleMemberId);

    @Query("select count(arrival) from CapsuleArrival arrival where arrival.capsuleMember.capsule.id = :capsuleId "
            + "and arrival.arrivalStatus = :arrivalStatus")
    long countByCapsuleIdAndArrivalStatus(
            @Param("capsuleId") Long capsuleId,
            @Param("arrivalStatus") ArrivalStatus arrivalStatus
    );

    @Modifying
    @Query("delete from CapsuleArrival arrival where arrival.capsuleMember.capsule.id = :capsuleId")
    void deleteAllByCapsuleId(@Param("capsuleId") Long capsuleId);
}
