package saigonuni.dev.resumeBuilder.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.User.CreateUserAdminRequest;
import saigonuni.dev.resumeBuilder.dto.User.UpdateUserAdminRequest;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;
import saigonuni.dev.resumeBuilder.exception.DuplicateKeyException;
import saigonuni.dev.resumeBuilder.exception.UserNotFoundException;
import saigonuni.dev.resumeBuilder.message.CommonMessage;
import saigonuni.dev.resumeBuilder.message.UserMessage;
import saigonuni.dev.resumeBuilder.repository.UserRepository;

@Service
@Slf4j
public class UserServiceImplement implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserValueService userValueService;

  @Override
  public User addUser(CreateUserAdminRequest request) {
    try {
      User user = User
        .builder()
        .username(request.getUsername())
        .password(request.getPassword())
        .email(request.getEmail())
        .role(request.getRole())
        .refreshToken(
          request.getRefreshToken() == null ? "" : request.getRefreshToken()
        )
        .createdAt(LocalDateTime.now())
        .build();

      User savedUser = userRepository.save(user);

      // Create UserValue for the newly created user

      return savedUser;
    } catch (DataIntegrityViolationException e) {
      throw new DuplicateKeyException(CommonMessage.DUPLICATE_KEY.getMessage());
    }
  }

  @Override
  public User getUserById(String id) {
    try {
      Optional<User> optionalUser = userRepository.findById(Long.valueOf(id));
      if (optionalUser.isEmpty()) {
        throw new BadRequestException(
          UserMessage.USER_NOT_FOUND_KEY,
          UserMessage.USER_NOT_FOUND_MESSAGE
        );
      }
      return optionalUser.get();
    } catch (NumberFormatException e) {
      throw new BadRequestException(
        "invalid_id",
        "The provided id is not valid."
      );
    } catch (Exception e) {
      throw new BadRequestException(
        UserMessage.USER_NOT_FOUND_KEY,
        UserMessage.USER_NOT_FOUND_MESSAGE
      );
    }
  }

  @Override
  public List<User> listUsers() {
    return userRepository.findAll();
  }

  // lay toan bo resume cua user
  // cac nay ko toi uu
  // die server
  public List<Resume> listAllResumes() { // Renamed for clarity
    // 1. Fetch all users
    List<User> users = userRepository.findAll();

    return users
      .stream() // Stream<User>
      .filter(user -> user.getUserValues() != null)
      .flatMap(user -> user.getUserValues().stream()) // Stream<UserValue> - Flattens the lists of userValues from all users
      .filter(userValue -> userValue.getResume() != null) // Optional: Safety check for null resume list
      .flatMap(userValue -> userValue.getResume().stream()) // Stream<Resume> - Flattens the lists of resumes from all userValues
      .collect(Collectors.toList()); // Collect all Resume objects into a single List<Resume>ume objects into a single List<Resume>
  }

  @Override
  public void deleteUser(String id) {
    try {
      User user = userRepository
        .findById(Long.valueOf(id))
        .orElseThrow(() -> new UserNotFoundException());

      userRepository.delete(user);
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public User updateUser(String userId, UpdateUserAdminRequest request) {
    try {
      User user = userRepository
        .findById(Long.valueOf(userId))
        .orElseThrow(() -> new UserNotFoundException());

      user.setUsername(request.getUsername());
      user.setPassword(request.getPassword());
      user.setEmail(request.getEmail());
      user.setRole(request.getRole());
      user.setUpdatedAt(LocalDateTime.now());

      return userRepository.save(user);
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public List<User> fetchCvByUserCreate() {
    return userRepository.findAllUsersWithUserValuesAndResumes();
  }

  @Override
  public List<Resume> findResumesByUserId(Long userId) {
    try {
      return userRepository.findResumesWithUserId(userId);
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println("Error: " + e.getMessage());
      throw e;
    }
  }

  @Override
  public Object[] fetchUserMakeCV() {
    return userRepository.fetchUserMakeCV();
  }

  @Override
  public List<Object> findResumesWithUserFullyRegister() {
    return userRepository.findResumesWithUserFullyRegister();
  }

  @Override
  public User deleteUserById(String id) {
    try {
      User user = userRepository
        .findById(Long.valueOf(id))
        .orElseThrow(() -> new UserNotFoundException());

      userRepository.delete(user);
      return user;
    } catch (Exception e) {
      throw e;
    }
  }
}
