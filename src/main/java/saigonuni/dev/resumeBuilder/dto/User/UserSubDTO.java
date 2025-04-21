package saigonuni.dev.resumeBuilder.dto.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSubDTO {

  public UserSubDTO(Long id, String username, String email) {
    this.id = id;
    this.username = username;
    this.email = email;
  }
  private Long id;
  private String username;
  private String email;
}
