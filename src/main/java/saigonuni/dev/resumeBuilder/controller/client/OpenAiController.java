package saigonuni.dev.resumeBuilder.controller.client;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import saigonuni.dev.resumeBuilder.dto.OpenAi.ChatResponse;
import saigonuni.dev.resumeBuilder.dto.OpenAi.DescriptionDTO;
import saigonuni.dev.resumeBuilder.dto.OpenAi.QueryRequest;
import saigonuni.dev.resumeBuilder.dto.OpenAi.SummaryCall;
import saigonuni.dev.resumeBuilder.dto.OpenAi.WorkExperience;
// Add this import if QueryRequest exists in your project
import saigonuni.dev.resumeBuilder.service.OpenAiResumeService;

@Tag(
  name = "OpenAi Call Controller",
  description = "Operations pertaining to Open Ai   management of Users"
)
@RestController
public class OpenAiController extends BaseController {

  private final OpenAiResumeService OpenService;
  private final CheckSubcriptionWithUserId checkSubcriptionWithUserId;
  private static RabbitTemplate rabbitTemplate;

  @Autowired
  public OpenAiController(
    OpenAiResumeService OpenService,
    CheckSubcriptionWithUserId checkSubcriptionWithUserId,
    RabbitTemplate rabbitTemplate
  ) {
    this.checkSubcriptionWithUserId = checkSubcriptionWithUserId;
    this.OpenService = OpenService;
    this.rabbitTemplate = rabbitTemplate;
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

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
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

  // @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  // @PostMapping("/api/agentAI/reviewCv")
  // public ResponseEntity<String> reviewCv(
  //   @Valid @RequestBody DescriptionDTO input,
  //   Principal principal
  // ) {
  //   if (principal == null) {
  //     throw new RuntimeException("User not authenticated.");
  //   }
  //   // checkSubcriptionWithUserId.checkPlanUsingAifeature(principal.getName());
  //   try {
  //     String review = OpenService.reviewCv(input);
  //     return ResponseEntity.ok(review);
  //   } catch (RuntimeException e) {
  //     // Basic error handling
  //     // You might want to return a specific error structure instead of WorkExperience
  //     return ResponseEntity.internalServerError().body(null); // Or a specific error object
  //   }
  // }
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping("/api/agentAI/reviewCv")
  public ResponseEntity<?> reviewCv(
    @Valid @RequestBody QueryRequest input,
    Principal principal
  ) {
    if (principal == null) {
      return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body("User not authenticated.");
    }

    try {
      Object responseObject = rabbitTemplate.convertSendAndReceive(
        "", // Default exchange
        "ragQueue", // Routing key (queue name)
        input // The QueryRequest object
      );

      // if (responseObject == null) {
      //   return ResponseEntity
      //     .internalServerError()
      //     .body(
      //       "Error reviewing CV: Unexpected response type from processing service."
      //     );
      // }

      // Assuming the responseObject is of type String
      // return ResponseEntity.ok(responseObject.toString());
      // } catch (Exception e) {
      //   return ResponseEntity
      //     .internalServerError()
      //     .body("Error reviewing CV: " + e.getMessage());
      // }
      if (responseObject instanceof ChatResponse) {
        ChatResponse chatResponse = (ChatResponse) responseObject;
        logger.info(
          "Received response from RAG service: {}",
          chatResponse.getResponse()
        );
        return ResponseEntity.ok(chatResponse); // Return the full ChatResponse or just parts of it
      } else if (responseObject == null) {
        logger.error(
          "Request to RAG service timed out or no response received."
        );
        return ResponseEntity
          .status(HttpStatus.REQUEST_TIMEOUT)
          .body(
            "Error reviewing CV: Request timed out or no response from processing service."
          );
      } else {
        logger.error(
          "Unexpected response type from RAG service: {}",
          responseObject.getClass().getName()
        );
        return ResponseEntity
          .internalServerError()
          .body(
            "Error reviewing CV: Unexpected response type from processing service."
          );
      }
    } catch (Exception e) { // Catch more specific AMQP exceptions if needed
      logger.error(
        "Error sending request to RAG service or processing reply",
        e
      );
      return ResponseEntity
        .internalServerError()
        .body("Error reviewing CV: " + e.getMessage());
    }
  }
}
