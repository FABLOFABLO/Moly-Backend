package moly.backend.domain.user.domain.repository;

import moly.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

    @Modifying
    @Query("update User user set user.memoCount = user.memoCount + :amount where user.id = :userId")
    void updateMemoCount(@Param("userId") Long userId, @Param("amount") long amount);
}
