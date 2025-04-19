package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.User.CreateUserAdminRequest;
import saigonuni.dev.resumeBuilder.dto.User.UpdateUserAdminRequest;

@Service
public interface UserService {
  User addUser(CreateUserAdminRequest request);

  User getUserById(String id);

  List<User> listUsers();

  void deleteUser(String id);

  User updateUser(String resumeId, UpdateUserAdminRequest request);
  List<User> fetchCvByUserCreate();
  List<Resume> findResumesByUserId(Long userId);
  Object[] fetchUserMakeCV();
  List<Object> findResumesWithUserFullyRegister();
 
}
