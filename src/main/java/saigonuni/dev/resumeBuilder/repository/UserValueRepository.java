package saigonuni.dev.resumeBuilder.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue;

@Repository
public interface UserValueRepository extends JpaRepository<UserValue, Long> {
  Optional<UserValue> findByUser(User user);

  // Corrected JPQL syntax: SELECT COUNT(...) and correct field access r.user.id
  @Query("SELECT COUNT(r) FROM UserValue r WHERE r.user.id = :userId")
  int CountCvCreatedByUserId(Long userId);
}
