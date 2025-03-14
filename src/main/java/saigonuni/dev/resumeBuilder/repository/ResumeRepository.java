package saigonuni.dev.resumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
  Resume save(Resume resume);
}
