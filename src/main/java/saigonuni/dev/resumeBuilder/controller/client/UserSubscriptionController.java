package saigonuni.dev.resumeBuilder.controller.client;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.common.Decorations.Decode;
import saigonuni.dev.resumeBuilder.common.Decorations.UserDC;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.dto.User.ListUserResponse;
// Removed unused import: import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSubcriptionResponse;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.DeleteResumeResponse;
import saigonuni.dev.resumeBuilder.service.JwtService;
import saigonuni.dev.resumeBuilder.service.UserSubcriptionService;

@Tag(
  name = "User Subcription Client Controller",
  description = "Operations pertaining user subcription "
)
@RestController
@RequestMapping("user")
public class UserSubscriptionController extends BaseController {

  @Autowired
  private UserSubcriptionService userSubcriptionService;

  private JwtService jwtService;

  private final UserDC userDC;
  private final Decode decode;

  @Autowired
  public UserSubscriptionController(
    UserSubcriptionService userSubcriptionService,
    JwtService jwtService,
    Decode decode,
    UserDC userDC
  ) {
    this.userSubcriptionService = userSubcriptionService;
    this.jwtService = jwtService;
    this.decode = decode;
    this.userDC = userDC;
  }

  // @GetMapping(
  //   value = "user-subscription-plan-registration",
  //   produces = { "application/json" }
  // )
  // public ResponseEntity<List<UserSupcriptionDTO>> fetchUserWithSpecialPlan( // Changed return type
  //   @RequestHeader("Authorization") String authorizationHeader
  // ) {
  //   User user = userDC.findUserNameByToken(
  //     decode.AuthenticationDecode(authorizationHeader)
  //   );
  //   List<UserSupcriptionDTO> userSubscriptionList = userSubcriptionService.fetchUserWithSpecialPlan(
  //     user.getId()
  //   );
  //   // Return the list directly
  //   return ResponseEntity.ok(userSubscriptionList);
  // }

  @GetMapping(
    value = "user-subscription-fetch",
    produces = { "application/json" }
  )
  public ResponseEntity<List<UserSubscription>> fetchDataUserSubciption( // Changed return type
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );
    List<UserSubscription> userSubscriptionList = userSubcriptionService.FetchDataUserSub();
    // Return the list directly
    return ResponseEntity.ok(userSubscriptionList);
  }
}
