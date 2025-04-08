package saigonuni.dev.resumeBuilder.dto.resume;

import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ResumeResponseDTO {

  private Resume resume;
  private UserValue userValue;
  private User user;
}
