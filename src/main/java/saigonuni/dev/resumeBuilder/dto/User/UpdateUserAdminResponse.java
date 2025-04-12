package saigonuni.dev.resumeBuilder.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.User;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateUserAdminResponse {

  private User user;
}
