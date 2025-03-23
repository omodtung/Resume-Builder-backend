package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.user.*;
import saigonuni.dev.resumeBuilder.exception.UserNotFoundException;
import saigonuni.dev.resumeBuilder.repository.UserRepository;

@Service
public class UserServiceImplement implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public User addUser(CreateUserAdminRequest request) {
    User user = User.builder()
      .username(request.getUsername())
      .password(request.getPassword())
      .email(request.getEmail())
      .role(request.getRole())
      .build();
    return userRepository.save(user);
  }

  @Override
  public User getUserById(String id) {
    Optional<User> optionalUser = userRepository.findById(id);
    return optionalUser.orElseThrow(UserNotFoundException::new);
  }

  @Override
  public List<User> listUsers() {
    return userRepository.findAll();
  }

  @Override
  public void deleteUserById(String id) {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    userRepository.delete(user);
  }

  @Override
  public User updateUser(String userId, UpdateUserAdminRequest request) {
    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());
    user.setEmail(request.getEmail());
    user.setRole(request.getRole());
    return userRepository.save(user);
  }
}
