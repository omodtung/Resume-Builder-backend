package saigonuni.dev.resumeBuilder.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.User;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetUserAdminResponse {
  private User user;
}
