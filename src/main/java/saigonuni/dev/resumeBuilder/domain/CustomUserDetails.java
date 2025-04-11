package saigonuni.dev.resumeBuilder.domain;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

  private String username; // This could be the email
  private String email; // Add email field
  private String password;

  // Other fields and methods...

  public String getEmail() {
    return email;
  }

  @Override
  public String getUsername() {
    return username; // Or return email if username is the email
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null; // Replace with actual authorities if needed
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true; // Replace with actual logic if needed
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // Replace with actual logic if needed
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // Replace with actual logic if needed
  }

  @Override
  public boolean isEnabled() {
    return true; // Replace with actual logic if needed
  }
}
