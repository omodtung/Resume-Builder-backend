package saigonuni.dev.resumeBuilder.controller.client;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.common.Decorations.Decode;
import saigonuni.dev.resumeBuilder.common.Decorations.UserDC;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.service.JwtService;
import saigonuni.dev.resumeBuilder.service.UserService;

@Tag(
  name = "User Client Site Controller",
  description = "Operations pertaining to User Client  management of Users"
)
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

  private final UserService userService;
  private JwtService jwtService;

  private final UserDC userDC;
  private final Decode decode;

  @Autowired
  public UserController(
    UserService userService,
    JwtService jwtService,
    Decode decode,
    UserDC userDC
  ) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.decode = decode;
    this.userDC = userDC;
  }

  @GetMapping("resumes/{userId}")
  public ResponseEntity<List<Resume>> getResumesByUserId(
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    try {
      User user = userDC.findUserNameByToken(
        decode.AuthenticationDecode(authorizationHeader)
      );

      List<Resume> resumes = userService.findResumesByUserId(user.getId());
      return ResponseEntity.ok(resumes);
    } catch (Exception e) {
      throw new RuntimeException("Error fetching resumes: " + e.getMessage());
    }
  }
}
