package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.common.Decorations.Decode;
import saigonuni.dev.resumeBuilder.common.Decorations.UserDC;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.domain.Plan;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.Plan.CreatePlanAdminRequest;
import saigonuni.dev.resumeBuilder.dto.Plan.CreatePlanAdminResponse;
import saigonuni.dev.resumeBuilder.dto.Plan.GetPlanAdminResponse;
import saigonuni.dev.resumeBuilder.dto.Plan.UpdatePlanAdminRequest;
import saigonuni.dev.resumeBuilder.dto.Plan.UpdatePlanAdminResponse;
import saigonuni.dev.resumeBuilder.service.JwtService;
import saigonuni.dev.resumeBuilder.service.PlanService;
import saigonuni.dev.resumeBuilder.service.PlanServiceImplement;

@Tag(
  name = "Plan Admin Controller",
  description = "Operations pertaining to admin management of plans"
)
@RestController
@RequestMapping("admin")
public class PlanController extends BaseController {

  private final JwtService jwtService;
  private final PlanService planService;
  private final UserDC userDC;
  private final Decode decode;

  @Autowired
  public PlanController(
    PlanServiceImplement planService,
    JwtService jwtService,
    Decode decode,
    UserDC userDC
  ) {
    this.planService = planService;
    this.jwtService = jwtService;
    this.decode = decode;
    this.userDC = userDC;
  }

  @PostMapping("plans")
  @Operation(
    summary = "API Thêm Plan mới",
    description = "Returns a list of all plans"
  )
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<CreatePlanAdminResponse> addPlan(
    @Valid @RequestBody CreatePlanAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    try {
      User user = userDC.findUserNameByToken(
        decode.AuthenticationDecode(authorizationHeader)
      );
      Plan plan = planService.addPlan(request, user);
      return ResponseEntity.ok(
        CreatePlanAdminResponse.builder().plan(plan).build()
      );
    } catch (Exception e) {
      throw new RuntimeException("Error processing request: " + e.getMessage());
    }
  }

  @GetMapping("plans/{id}")
  @LogExecutionTime
  public ResponseEntity<GetPlanAdminResponse> getPlanById(
    @PathVariable Long id
  ) {
    Plan plan = planService.getPlanById(id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(GetPlanAdminResponse.builder().plan(plan).build());
  }

  @GetMapping("plans")
  public ResponseEntity<List<Plan>> getPlans() {
    List<Plan> plans = planService.listPlans();
    return ResponseEntity.ok(plans);
  }

  @PostMapping("plans/{id}")
  @Operation(summary = "API Update Plan ", description = "Update API Plan")
  @LogExecutionTime
  public ResponseEntity<UpdatePlanAdminResponse> updatePlan(
    @PathVariable Long id,
    @Valid @RequestBody UpdatePlanAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );
    Plan plan = planService.updatePlan(id, request, user);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(UpdatePlanAdminResponse.builder().plan(plan).build());
  }

  // @DeleteMapping("/{id}")
  // public void deletePlan(@PathVariable Long id) {
  //   planService.deletePlan(id);
  // }
}
