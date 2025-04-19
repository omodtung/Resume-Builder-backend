package saigonuni.dev.resumeBuilder.dto.UserJpaDTO;

import java.time.LocalDateTime;

public class User {

  private Long id;
  private String username;
  private String email;
  private String role;
  private LocalDateTime createdAt;

  // constructor
  public User(
    Long id,
    String username,
    String email,
    String role,
    LocalDateTime createdAt
  ) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
    this.createdAt = createdAt;
  }
}
