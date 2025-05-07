package saigonuni.dev.resumeBuilder.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // Add this import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jdk.jfr.Description;
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
import saigonuni.dev.resumeBuilder.repository.UserSubscriptionRepository;
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

  @Autowired
  private UserSubscriptionRepository userSubcriptionRepository;

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

  @GetMapping(value = "user-subscription-follow-userId")
  @Operation(
    summary = "API find Plans depend on subcription user have to check",
    description = "check user if user have this plan to do a cv creation or feature creation"
  )
  public ResponseEntity<List<UserSupcriptionDTO>> fetchDataUserSubciptionTest( // Changed return type
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );
    List<UserSupcriptionDTO> userSubscriptionList = userSubcriptionService.FetchDataUserSubWithPlanWithUser(
      user.getId()
    );
    return ResponseEntity.ok(userSubscriptionList);
  }

  @GetMapping(
    value = "user-subscription-fetch",
    produces = { "application/json" }
  )
  public ResponseEntity<List<UserSubscription>> fetchDataUserSubciption( 
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );
    List<UserSubscription> userSubscriptionList = userSubcriptionService.FetchDataUserSub();
    return ResponseEntity.ok(userSubscriptionList);
  }

  @GetMapping(value = "user-subscription")
  public ResponseEntity<Map<String, Object>> fetchDataUserSubciption(
    @RequestParam(required = false) String sort,
    @RequestParam(required = false) String order,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int limit
  ) {
    try {
      List<UserSubscription> usersSub; // Initialize later
      Sort.Direction direction = "asc".equalsIgnoreCase(order)
        ? Sort.Direction.ASC  
        : Sort.Direction.DESC;
      Pageable paging;

      if (sort != null && !sort.isEmpty()) {
        paging = PageRequest.of(page, limit, Sort.by(direction, sort));
      } else {
        paging = PageRequest.of(page, limit); // Default paging without sort
      }

      Page<UserSubscription> pageTuts = userSubcriptionRepository.findAll(
        paging
      );

      usersSub = pageTuts.getContent();

      List<Map<String, Object>> usersSub1 = new ArrayList<>();
      for (UserSubscription user : pageTuts.getContent()) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("stripeCustomerId", user.getStripeCustomerId());
        userMap.put("stripeSubscriptionId", user.getStripeSubscriptionId());
        userMap.put("stripeCurrentPeriodEnd", user.getStripeCurrentPeriodEnd());
        userMap.put(
          "stripeCancelAtPeriodEnd",
          user.getStripeCancelAtPeriodEnd()
        );
        userMap.put("user", user.getUser());
        userMap.put("plan", user.getPlan());
        usersSub1.add(userMap);
      }
      Map<String, Object> response = new HashMap<>();
      response.put("data", usersSub);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());

      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
