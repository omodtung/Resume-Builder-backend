package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.user.*;

@Service
public interface UserService {
  User addUser(CreateUserAdminRequest request);
  User getUserById(String id);
  List<User> listUsers();
  void deleteUserById(String id);
  User updateUser(String userId, UpdateUserAdminRequest request);
}
