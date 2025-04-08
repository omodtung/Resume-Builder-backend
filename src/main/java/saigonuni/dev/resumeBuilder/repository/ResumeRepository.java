package saigonuni.dev.resumeBuilder.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, String> {
  Resume save(Resume resume);
  Resume findById(Resume resume);

  @Query("SELECT r FROM Resume r WHERE r.userValue.user.id = :userId")
  List<Resume> findByUserId(@Param("userId") Long userId);
}
