package saigonuni.dev.resumeBuilder.dto.resume;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.Resume;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ListResumeResponse {

  private List<Resume> resume;
}
