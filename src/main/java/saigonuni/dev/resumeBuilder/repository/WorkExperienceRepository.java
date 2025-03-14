package saigonuni.dev.resumeBuilder.repository;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;

public interface WorkExperienceRepository
  extends JpaRepository<WorkExperience, Long> {
  WorkExperience save(WorkExperience workExperience);
}
