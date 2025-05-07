package saigonuni.dev.resumeBuilder.controller.client;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.common.validate.CheckSubcriptionWithUserId;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.dto.OpenAi.DescriptionDTO;
import saigonuni.dev.resumeBuilder.dto.OpenAi.SummaryCall;
import saigonuni.dev.resumeBuilder.dto.OpenAi.WorkExperience;
import saigonuni.dev.resumeBuilder.service.OpenAiResumeService;

@Tag(
  name = "OpenAi Call Controller",
  description = "Operations pertaining to Open Ai   management of Users"
)
@RestController
public class OpenAiController extends BaseController {

  private final OpenAiResumeService OpenService;
  private final CheckSubcriptionWithUserId checkSubcriptionWithUserId;

  @Autowired
  public OpenAiController(
    OpenAiResumeService OpenService,
    CheckSubcriptionWithUserId checkSubcriptionWithUserId
  ) {
    this.checkSubcriptionWithUserId = checkSubcriptionWithUserId;
    this.OpenService = OpenService;
  }

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping("api/openai/summary")
  public ResponseEntity<String> generateSummary(
    @Valid @RequestBody SummaryCall input,
    Principal principal
  ) {
    if (principal == null) {
      return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body("User not authenticated.");
    }
    // checkSubcriptionWithUserId.checkPlanUsingAifeature(principal.getName());
    try {
      String summary = OpenService.generateSummary(input);
      return ResponseEntity.status(HttpStatus.OK).body(summary);
    } catch (RuntimeException e) {
      // Basic error handling
      e.printStackTrace(); // Log the full stack trace
      return ResponseEntity
        .internalServerError()
        .body("Error generating summary: " + e.getMessage());
    }
  }

  @PostMapping("/api/openai/work-experience")
  public ResponseEntity<WorkExperience> generateWorkExperience(
    @Valid @RequestBody DescriptionDTO input,
    Principal principal
  ) {
    if (principal == null) {
      throw new RuntimeException("User not authenticated.");
    }
    // checkSubcriptionWithUserId.checkPlanUsingAifeature(principal.getName());
    try {
      WorkExperience workExperience = OpenService.generateWorkExperience(input);
      return ResponseEntity.ok(workExperience);
    } catch (RuntimeException e) {
      // Basic error handling
      // You might want to return a specific error structure instead of WorkExperience
      return ResponseEntity.internalServerError().body(null); // Or a specific error object
    }
  }
}
