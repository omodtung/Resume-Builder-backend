package saigonuni.dev.resumeBuilder.controller.admin;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.User.CreateUserAdminRequest;
import saigonuni.dev.resumeBuilder.dto.User.CreateUserAdminResponse;
import saigonuni.dev.resumeBuilder.repository.UserRepository;
import saigonuni.dev.resumeBuilder.service.CustomUserDetailsService;
import saigonuni.dev.resumeBuilder.utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<CreateUserAdminResponse> register(
    @Valid @RequestBody CreateUserAdminRequest request
  ) {
    // Check if the username already exists
    if (userRepository.existsByUsername(request.getUsername())) {
      return ResponseEntity
        .badRequest()
        .body(
          CreateUserAdminResponse
            .builder()
            .user(null)
            .message("Username already exists")
            .build()
        );
    }

    // Check if the email already exists
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity
        .badRequest()
        .body(
          CreateUserAdminResponse
            .builder()
            .user(null)
            .message("Email already exists")
            .build()
        );
    }

    // Encode the password before saving
    User user = User
      .builder()
      .username(request.getUsername())
      .password(passwordEncoder.encode(request.getPassword())) // Encrypt the password
      .email(request.getEmail())
      .role("USER") // Default role
      .refreshToken(request.getRefreshToken() == null ? "" : request.getRefreshToken())
      .createdAt(LocalDateTime.now())
      .build();

    User savedUser = userRepository.save(user);

    return ResponseEntity.ok(
      CreateUserAdminResponse
        .builder()
        .user(savedUser)
        .message("User registered successfully")
        .build()
    );
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(
    @RequestParam String username,
    @RequestParam String password
  ) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(username, password)
    );
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    String token = jwtUtil.generateToken(userDetails.getUsername());
    return ResponseEntity.ok(token);
  }
}
