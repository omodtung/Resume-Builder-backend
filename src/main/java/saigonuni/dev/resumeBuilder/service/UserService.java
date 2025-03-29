package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminRequest;

@Service
public interface UserService {
  User addUser(CreateResumeAdminRequest request);

  User getUserById(String id);

  List<User> listUsers();

  void deleteUser(String id);

  User updateUser(String resumeId, UpdateResumeAdminRequest request);
}
