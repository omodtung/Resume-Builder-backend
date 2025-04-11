package saigonuni.dev.resumeBuilder.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.UserValue;
import saigonuni.dev.resumeBuilder.domain.User ;
@Repository
public interface UserValueRepository extends JpaRepository<UserValue, Long> {
  Optional<UserValue> findByUser(User user);
  
}
