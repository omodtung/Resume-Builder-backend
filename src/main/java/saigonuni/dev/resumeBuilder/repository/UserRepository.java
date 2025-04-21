package saigonuni.dev.resumeBuilder.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // User findByUserName(String UserName);
  User findByUsername(String username);
  boolean existsByUsername(String username);
  Optional<User> findByEmail(String email);

  
  // User findByEmail(String email);

  // @Query(value = "SELECT u.* FROM users u " +
  //         "LEFT JOIN user_value uv ON u.id = uv.user_id " +
  //         "LEFT JOIN resumes r ON uv.id = r.user_value_id", nativeQuery = true)
  // List<User> findAllUsersWithUserValuesAndResumes();

  @Query(
    value = "SELECT u.*, uv.*, r.* FROM users u " +
    "LEFT JOIN users_values uv ON u.id = uv.user_id " +
    "LEFT JOIN resumes r ON uv.id = r.user_value_id",
    nativeQuery = true
  )
  List<User> findAllUsersWithUserValuesAndResumes();

  @Query(
    value = "SELECT r.* FROM users u " +
    "INNER JOIN users_values uv ON u.id = uv.user_id " +
    "INNER JOIN resumes r ON uv.id = r.user_value_id " +
    "WHERE u.id = :userId", // Thêm điều kiện WHERE
    nativeQuery = true
  )
  List<Object[]> fetchUserMakeCVWithUserID(Long userId);

  @Query(
    value = "SELECT r.* FROM users u " +
    "INNER JOIN users_values uv ON u.id = uv.user_id " +
    "INNER JOIN resumes r ON uv.id = r.user_value_id ",
    nativeQuery = true
  )
  Object[] fetchUserMakeCV();

  @Query(
    "SELECT r FROM Resume r JOIN r.userValue uv JOIN uv.user u WHERE u.id = :userId"
  )
  List<Resume> findResumesWithUserId(@Param("userId") Long userId);

  // @Query("SELECT r FROM Resume r JOIN r.userValue uv JOIN uv.user u")
  @Query("SELECT u FROM User u JOIN u.userValues uv JOIN uv.resume r")
  List<Object> findResumesWithUserFullyRegister();

  Page<User> findByUsernameContaining(String username, Pageable pageable);


}
