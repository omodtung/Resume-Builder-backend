package saigonuni.dev.resumeBuilder.controller.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.common.validate.CheckSubcriptionWithUserId;
import saigonuni.dev.resumeBuilder.controller.admin.UploadFileController;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.dto.OpenAi.DescriptionDTO;
import saigonuni.dev.resumeBuilder.dto.OpenAi.ObjectModelAI;
import saigonuni.dev.resumeBuilder.dto.OpenAi.QueryRequest;
import saigonuni.dev.resumeBuilder.dto.OpenAi.SummaryCall;
import saigonuni.dev.resumeBuilder.dto.OpenAi.WorkExperience;
import saigonuni.dev.resumeBuilder.service.OpenAiResumeService;
import saigonuni.dev.resumeBuilder.service.UploadServiceImplement;

@Tag(
  name = "OpenAi Call Controller",
  description = "Operations pertaining to Open Ai   management of Users"
)
@RestController
public class OpenAiController extends BaseController {

  private final OpenAiResumeService OpenService;
  private final CheckSubcriptionWithUserId checkSubcriptionWithUserId;
  private static RabbitTemplate rabbitTemplate;
  private static UploadFileController uploadFileController;

  @Autowired // Đảm bảo bạn có dòng này để inject ObjectMapper
  private ObjectMapper objectMapper;

  @Autowired
  public OpenAiController(
    OpenAiResumeService OpenService,
    CheckSubcriptionWithUserId checkSubcriptionWithUserId,
    RabbitTemplate rabbitTemplate,
    UploadFileController uploadFileController
  ) {
    this.checkSubcriptionWithUserId = checkSubcriptionWithUserId;
    this.OpenService = OpenService;
    this.rabbitTemplate = rabbitTemplate;
    this.uploadFileController = uploadFileController;
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
    checkSubcriptionWithUserId.checkPlanUsingAifeature(principal.getName());
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

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping("/api/openai/work-experience")
  public ResponseEntity<WorkExperience> generateWorkExperience(
    @Valid @RequestBody DescriptionDTO input,
    Principal principal
  ) {
    if (principal == null) {
      throw new RuntimeException("User not authenticated.");
    }
    checkSubcriptionWithUserId.checkPlanUsingAifeature(principal.getName());
    try {
      WorkExperience workExperience = OpenService.generateWorkExperience(input);
      return ResponseEntity.ok(workExperience);
    } catch (RuntimeException e) {
      // Basic error handling
      // You might want to return a specific error structure instead of WorkExperience
      return ResponseEntity.internalServerError().body(null); // Or a specific error object
    }
  }

  // to do response format again
  @CrossOrigin(origins = "http://localhost:3000")
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping("/api/agentAI/reviewCv")
  public ResponseEntity<?> reviewCv(@RequestBody QueryRequest input) {
    try {
      Object chatResponse = rabbitTemplate.convertSendAndReceive(
        "",
        "ragQueue",
        input
      );

      System.out.println("ChatResponse after Decode " + chatResponse);
      if (chatResponse != null) {
        return ResponseEntity.ok(chatResponse);
      } else {
        return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .body("No response received.");
      }
    } catch (Exception e) {
      return ResponseEntity
        .internalServerError()
        .body("Error reviewing CV: " + e.getMessage());
    }
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping("/api/agentAI/match")
  public ResponseEntity<QueryRequest> rateCv(@RequestBody QueryRequest input) {
    try {
      Object chatResponse = rabbitTemplate.convertSendAndReceive(
        "",
        "ragSkillQueue",
        input
      );

      QueryRequest skillExtractionResult = objectMapper.convertValue(
        chatResponse,
        QueryRequest.class
      );
      String extractedSkills = skillExtractionResult.getQuery();
      System.out.println("Validate Match company" + extractedSkills);
      QueryRequest process = uploadFileController.sendSkillToModelAi(
        extractedSkills,
        skillExtractionResult.getUserId()
      );

      if (chatResponse != null) {
        return ResponseEntity.ok(process);
      } else {
        return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .body(null);
      }
    } catch (Exception e) {
      return ResponseEntity
        .internalServerError()
        .body(null);
    }
  }
}
