package saigonuni.dev.resumeBuilder.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {
    saigonuni.dev.resumeBuilder.domain.User domainUser = userRepository //
      .findByEmail(email)
      .orElseThrow(() ->
        new UsernameNotFoundException("User not found with email: " + email)
      );

    // log.info("User roles: {}", domainUser.getRole());
    return new org.springframework.security.core.userdetails.User(
      domainUser.getUsername(), // Use email as the username
      domainUser.getPassword(),
      domainUser.getAuthorities() // Return the user's authorities
    );
  }
}
