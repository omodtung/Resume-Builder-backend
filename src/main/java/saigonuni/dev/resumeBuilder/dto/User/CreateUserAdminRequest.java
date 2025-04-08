package saigonuni.dev.resumeBuilder.dto.User;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserAdminRequest {

  @NotEmpty(message = "Username cannot be empty")
  private String username;

  @NotEmpty(message = "Password cannot be empty")
  private String password;

  @NotEmpty(message = "Email cannot be empty")
  private String email;

  private String refreshToken;

  private String role;
  // private LocalDateTime createdAt;

}
