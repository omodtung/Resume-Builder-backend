package saigonuni.dev.resumeBuilder.dto.resume;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateResumeAdmin {

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String title;

  @NotEmpty
  private String description;
}
