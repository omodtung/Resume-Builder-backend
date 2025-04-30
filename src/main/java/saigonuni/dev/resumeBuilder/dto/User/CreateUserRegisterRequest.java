package saigonuni.dev.resumeBuilder.dto.User;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRegisterRequest {

  @NotEmpty(message = "Username cannot be empty")
  private String username;

  @NotEmpty(message = "Password cannot be empty")
  private String password;

  @NotEmpty(message = "Email cannot be empty")
  private String email;

  private String refreshToken;
}
