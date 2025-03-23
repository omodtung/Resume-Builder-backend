package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.user.*;
import saigonuni.dev.resumeBuilder.service.UserService;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "User Admin Controller", description = "APIs for managing users")
public class UserAdminController extends BaseController {

  private final UserService userService;

  @Autowired
  public UserAdminController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  @Operation(
    summary = "API to add a new user",
    description = "Returns the created user"
  )
  public ResponseEntity<CreateUserAdminResponse> addUser(
    @Valid @RequestBody CreateUserAdminRequest request
  ) {
    User user = userService.addUser(request);
    return ResponseEntity.ok(
      CreateUserAdminResponse.builder().user(user).build()
    );
  }

  @GetMapping("/users/{id}")
  @Operation(
    summary = "API to get a user by ID",
    description = "Returns the user with the specified ID"
  )
  public ResponseEntity<GetUserAdminResponse> getUserById(
    @PathVariable String id
  ) {
    User user = userService.getUserById(id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(GetUserAdminResponse.builder().user(user).build());
  }

  @GetMapping("/users")
  @Operation(
    summary = "API to get all users",
    description = "Returns a list of all users"
  )
  public ResponseEntity<ListUserResponse> getUsers() {
    List<User> users = userService.listUsers();
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(ListUserResponse.builder().users(users).build());
  }

  @PostMapping("/users/{id}")
  @Operation(summary = "API to update a user", description = "Update a user")
  @LogExecutionTime
  public ResponseEntity<UpdateUserAdminResponse> updateUser(
    @PathVariable String id,
    @RequestBody UpdateUserAdminRequest request
  ) {
    User user = userService.updateUser(id, request);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(UpdateUserAdminResponse.builder().user(user).build());
  }

  @DeleteMapping("/users/{id}")
  @Operation(
    summary = "API to delete a user by ID",
    description = "Deletes the user with the specified ID"
  )
  public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
    userService.deleteUserById(id);
    return ResponseEntity.noContent().build();
  }
}
