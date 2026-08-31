package moly.backend.domain.capsule.domain.repository;

import moly.backend.domain.capsule.domain.CapsuleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CapsuleImageRepository extends JpaRepository<CapsuleImage, Long> {
    List<CapsuleImage> findAllByCapsule_Id(Long capsuleId);

    @Modifying
    @Query("delete from CapsuleImage image where image.capsule.id = :capsuleId")
    void deleteAllByCapsuleId(@Param("capsuleId") Long capsuleId);
}
