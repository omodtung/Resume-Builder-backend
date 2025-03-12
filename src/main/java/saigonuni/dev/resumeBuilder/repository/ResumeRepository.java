import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Resume save(Resume resume);
}
