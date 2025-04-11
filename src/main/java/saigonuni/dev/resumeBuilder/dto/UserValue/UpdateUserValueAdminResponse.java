package saigonuni.dev.resumeBuilder.dto.UserValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateUserValueAdminResponse {

  private UserValue userValue;
}
