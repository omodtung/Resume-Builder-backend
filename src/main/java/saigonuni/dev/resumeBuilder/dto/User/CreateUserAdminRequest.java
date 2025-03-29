package saigonuni.dev.resumeBuilder.dto.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import saigonuni.dev.resumeBuilder.domain.UserValue;

@Data
@Builder
public class CreateUserAdminRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "Username cannot be empty")
  @Size(max = 50, message = "Username should not exceed 50 characters")
  @Column(nullable = false, unique = true)
  private String username;

  @NotEmpty(message = "Password cannot be empty")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Column(nullable = false)
  private String password;

  @NotEmpty(message = "Email cannot be empty")
  @Email(message = "Email should be a valid email address")
  @Column(nullable = false, unique = true)
  private String email;

  @NotEmpty(message = "Role cannot be empty")
  @Pattern(
    regexp = "^(ADMIN|USER)$",
    message = "Role must be either ADMIN or USER"
  )
  @Column(nullable = false)
  private String role;

  @Column(name = "refresh_token")
  private String refreshToken;

  @NotNull(message = "CreatedAt cannot be null")
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = true)
  private LocalDateTime updatedAt;
}
