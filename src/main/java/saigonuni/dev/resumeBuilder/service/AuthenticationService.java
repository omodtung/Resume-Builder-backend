package saigonuni.dev.resumeBuilder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.Auth.AuthenticationRequest;
import saigonuni.dev.resumeBuilder.dto.Auth.AutheticationResponse;
import saigonuni.dev.resumeBuilder.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository UserRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AutheticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
      )
    );
    System.out.println("User found email  Ser" + request.getEmail());
    System.out.println("User found passowrd  Ser" + request.getPassword());
    User user = UserRepository.findByEmail(request.getEmail()).orElseThrow();
    System.out.println("User found " + user);

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(user.getEmail(), refreshToken);
    return AutheticationResponse
      .builder()
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .userId(user.getId()) // Include userId in the response
      .build();
  }

  private void saveUserToken(String email, String jwtTokenString) {
    // Retrieve the existing user by email
    User user = UserRepository
      .findByEmail(email)
      .orElseThrow(() ->
        new IllegalArgumentException("User not found with email: " + email)
      );

    // Update the refreshToken field
    user.setRefreshToken(jwtTokenString);

    // Save the updated user back to the database
    UserRepository.save(user);
  }

  public void refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      User user = this.UserRepository.findByEmail(userEmail).orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        String accessToken = jwtService.generateToken(user);

        saveUserToken(user.getEmail(), accessToken);
        AutheticationResponse authResponse = AutheticationResponse
          .builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
