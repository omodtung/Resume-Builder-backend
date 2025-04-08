package saigonuni.dev.resumeBuilder.dto.UserValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserValueAdminResponse {

  private UserValue userValue;
}
