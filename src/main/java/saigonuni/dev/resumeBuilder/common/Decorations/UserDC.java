package saigonuni.dev.resumeBuilder.common.Decorations;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.repository.UserRepository;

@Component
public class UserDC {

  private final UserRepository userRepository;

  public UserDC(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findUserNameByToken(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      System.err.println("User not found");
      return null;
    }
    System.err.println("User found DC: " + user);
    return user;
  }
}
