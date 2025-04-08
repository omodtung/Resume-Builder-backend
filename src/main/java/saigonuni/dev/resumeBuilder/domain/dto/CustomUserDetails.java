package saigonuni.dev.resumeBuilder.domain.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private Long id;
  private String username;
  private String email;
  private String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList(); // Return empty authorities for now
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true; // Assume account is not expired
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // Assume account is not locked
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // Assume credentials are not expired
  }

  @Override
  public boolean isEnabled() {
    return true; // Assume user is enabled
  }
}
