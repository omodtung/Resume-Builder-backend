package saigonuni.dev.resumeBuilder.dto.user;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.User;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ListUserResponse {
  private List<User> users;
}
