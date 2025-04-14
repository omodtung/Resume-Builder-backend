package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.User.CreateUserAdminRequest;
import saigonuni.dev.resumeBuilder.dto.User.CreateUserAdminResponse;
import saigonuni.dev.resumeBuilder.dto.User.GetUserAdminResponse;
import saigonuni.dev.resumeBuilder.dto.User.ListUserResponse;
import saigonuni.dev.resumeBuilder.dto.User.UpdateUserAdminRequest;
import saigonuni.dev.resumeBuilder.dto.User.UpdateUserAdminResponse;
import saigonuni.dev.resumeBuilder.repository.UserRepository;
import saigonuni.dev.resumeBuilder.service.UserService;

@Tag(
  name = "User Admin Controller",
  description = "Operations pertaining to admin management of Users"
)
@RestController
@RequestMapping("admin")
public class UserAdminController {

  private final UserService userService;

  @Autowired
  public UserAdminController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("users")
  @Operation(summary = "Add a new user", description = "Creates a new user")
  public ResponseEntity<CreateUserAdminResponse> addUser(
    @Valid @RequestBody CreateUserAdminRequest request
  ) {
    User user = userService.addUser(request);
    // return ResponseEntity.status(HttpStatus.CREATED).body(user);
    return ResponseEntity.ok(
      CreateUserAdminResponse.builder().user(user).build()
    );
  }

  @GetMapping("users/{id}")
  @Operation(summary = "Get user by ID", description = "Fetches a user by ID")
  public ResponseEntity<GetUserAdminResponse> getUserById(
    @PathVariable String id
  ) {
    User user = userService.getUserById(id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(GetUserAdminResponse.builder().user(user).build());
  }

  @GetMapping("users")
  @Operation(summary = "List all users", description = "Fetches all users")
  public ResponseEntity<ListUserResponse> listUsers() {
    List<User> users = userService.listUsers();
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(ListUserResponse.builder().user(users).build());
  }

  @PutMapping("users/{id}")
  @Operation(summary = "Update user", description = "Updates an existing user")
  public ResponseEntity<UpdateUserAdminResponse> updateUser(
    @PathVariable String id,
    @Valid @RequestBody UpdateUserAdminRequest request
  ) {
    User user = userService.updateUser(id, request);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(UpdateUserAdminResponse.builder().user(user).build());
  }

  @DeleteMapping("users/{id}")
  @Operation(summary = "Delete user", description = "Deletes a user by ID")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  // @GetMapping("users/cv")
  // public List<User> fetchCvByUserCreate() {
  //   return userService.fetchCvByUserCreate();
  // }

  @GetMapping("users/resumes/{userId}")
  public ResponseEntity<List<Resume>> getResumesByUserId(
    @PathVariable Long userId
  ) {
    List<Resume> resumes = userService.findResumesByUserId(userId);
    return ResponseEntity.ok(resumes);
  }

  @GetMapping("user-created-cv")
  public Object[] getResumesByUserId() {
    return userService.fetchUserMakeCV();
  }

  @GetMapping("resumes-usersRegisted")
  public List<Object> getResumesByUser() {
    try {
      return userService.findResumesWithUserFullyRegister();
    } catch (Exception e) {
      throw new RuntimeException("Error fetching resumes: " + e.getMessage());
    }
  }
}
