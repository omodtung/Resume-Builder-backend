package saigonuni.dev.resumeBuilder.dto.workExperience;

import java.time.LocalDate;
import saigonuni.dev.resumeBuilder.domain.Resume;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class WorkExperienceDomainResponse {

  private Long id;
  private String position;
  private String company;
  private LocalDate startDate;
  private LocalDate endDate;
  private String description;
  private Resume resume;
}
