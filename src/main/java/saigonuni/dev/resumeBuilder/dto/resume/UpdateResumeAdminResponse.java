package saigonuni.dev.resumeBuilder.dto.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.Resume;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResumeAdminResponse {

  private Resume resume;
}
