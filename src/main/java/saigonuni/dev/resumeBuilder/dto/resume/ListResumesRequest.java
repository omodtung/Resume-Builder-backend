package saigonuni.dev.resumeBuilder.dto.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.Resume;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ListResumesRequest {

  private long pivotId;
  private int prevLimit;
  private int nextLimit;
}
