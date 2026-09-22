package moly.backend.domain.user.domain.repository;

import jakarta.persistence.LockModeType;
import moly.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select user from User user where user.id in :userIds order by user.id")
    List<User> findAllByIdForUpdate(@Param("userIds") Collection<Long> userIds);

    @Modifying
    @Query("update User user set user.memoCount = user.memoCount + :amount where user.id = :userId")
    void updateMemoCount(@Param("userId") Long userId, @Param("amount") long amount);
}
