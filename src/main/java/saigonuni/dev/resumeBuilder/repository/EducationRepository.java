package saigonuni.dev.resumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Education;
public interface EducationRepository extends JpaRepository<Education, Long> {
  Education save(Education education);
    
}
