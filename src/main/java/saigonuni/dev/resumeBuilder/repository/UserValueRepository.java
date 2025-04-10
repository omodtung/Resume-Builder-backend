package saigonuni.dev.resumeBuilder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue ;
@Repository
public interface UserValueRepository extends JpaRepository<UserValue, Long> {
  Optional<UserValue> findByUser(User user);
  Optional<UserValue> findByUserId(Long userId);
  
}
