package saigonuni.dev.resumeBuilder.dto.User;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserAdminRequest {

  private Long id;

  @NotEmpty(message = "Username cannot be empty")
  private String username;

  @NotEmpty(message = "Password cannot be empty")
  private String password;

  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotEmpty(message = "Role cannot be empty")
  private String role;

  private String refreshToken;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
