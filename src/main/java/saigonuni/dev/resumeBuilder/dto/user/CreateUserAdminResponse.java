package saigonuni.dev.resumeBuilder.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserAdminResponse {

  private User user;
  private String message;
}
