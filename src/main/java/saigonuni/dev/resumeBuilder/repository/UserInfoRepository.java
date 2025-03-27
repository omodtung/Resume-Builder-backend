package saigonuni.dev.resumeBuilder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import saigonuni.dev.resumeBuilder.domain.User;
public interface UserInfoRepository extends JpaRepository<User, Long> {
      Optional<User> findByEmail(String email); // Use 'email' if that is the
}
