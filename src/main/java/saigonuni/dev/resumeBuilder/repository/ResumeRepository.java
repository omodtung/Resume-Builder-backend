package saigonuni.dev.resumeBuilder.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, String> {
  Resume save(Resume resume);
}
