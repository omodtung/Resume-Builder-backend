package saigonuni.dev.resumeBuilder.controller.admin;

import com.stripe.exception.StripeException;
import com.stripe.service.PlanService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.common.Decorations.Decode;
import saigonuni.dev.resumeBuilder.common.Decorations.UserDC;
import saigonuni.dev.resumeBuilder.domain.Plan;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.Stripe.StripeRequest;
import saigonuni.dev.resumeBuilder.dto.Stripe.StripeResponse;
import saigonuni.dev.resumeBuilder.service.JwtService;
import saigonuni.dev.resumeBuilder.service.PlanServiceImplement;
import saigonuni.dev.resumeBuilder.service.ResumeService;
import saigonuni.dev.resumeBuilder.service.StripeService;

@RestController
public class CheckOutController {

  @Autowired
  private StripeService stripeService;

  @Autowired
  private PlanServiceImplement planServiceImp;

  private JwtService jwtService;
  private final UserDC userDC;
  private final Decode decode;

  public CheckOutController(
    StripeService stripeService,
    JwtService jwtService,
    Decode decode,
    UserDC userDC
  ) {
    this.stripeService = stripeService;
    this.jwtService = jwtService;
    this.decode = decode;
    this.userDC = userDC;
  }
//  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping("user/checkout-payment")
  public ResponseEntity<StripeResponse> checkoutProducts(
    @RequestParam("PlanId") Long PlanId,
    @RequestHeader("Authorization") String authorizationHeader
  ) throws StripeException {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );

    Plan plan = planServiceImp.findStripePriceByPlanId(PlanId);

    StripeResponse stripeResponse = stripeService.createCheckoutSession(
      plan.getStripePriceId(),
      user
    );
    return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
  }
}
