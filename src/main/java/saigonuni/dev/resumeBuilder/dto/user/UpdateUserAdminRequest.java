package saigonuni.dev.resumeBuilder.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserAdminRequest {

  @NotEmpty(message = "Username cannot be empty")
  private String username;

  @NotEmpty(message = "Password cannot be empty")
  private String password;

  @Email(message = "Email should be a valid email address")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotEmpty(message = "Role cannot be empty")
  private String role;
}
