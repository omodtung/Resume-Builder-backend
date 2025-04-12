package saigonuni.dev.resumeBuilder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import saigonuni.dev.resumeBuilder.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // User findByUserName(String UserName);
  User findByUsername(String username);
  boolean existsByUsername(String username);
  Optional<User> findByEmail(String email);

  @Query(
    value = "SELECT * FROM users WHERE email = :email",
    nativeQuery = true
  )
  Optional<User> findByEmailNative(String email);

  @Query(
    value = "SELECT * FROM plan WHERE plans_name = :plansName",
    nativeQuery = true
  )
  void findResumeByUserId(User user);
  // User findByEmail(String email);
}
