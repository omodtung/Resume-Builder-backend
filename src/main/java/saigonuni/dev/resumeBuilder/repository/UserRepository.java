package saigonuni.dev.resumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUserName(String UserName);
}
