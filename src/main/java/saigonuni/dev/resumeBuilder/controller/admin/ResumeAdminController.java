package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.common.Decorations.Decode;
import saigonuni.dev.resumeBuilder.common.Decorations.UserDC;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.DeleteResumeResponse;
import saigonuni.dev.resumeBuilder.dto.resume.EditResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.GetResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminResponse;
import saigonuni.dev.resumeBuilder.service.JwtService;
import saigonuni.dev.resumeBuilder.service.ResumeService;

@Tag(
  name = "Resume Admin Controller",
  description = "Operations pertaining to admin management of resumes"
)
@RestController
@RequestMapping("admin")
public class ResumeAdminController extends BaseController {

  private JwtService jwtService;
  private final ResumeService resumeService;
  private final UserDC userDC;
  private final Decode decode;

  @Autowired
  public ResumeAdminController(
    ResumeService resumeService,
    JwtService jwtService,
    Decode decode,
    UserDC userDC
  ) {
    this.resumeService = resumeService;
    this.jwtService = jwtService;
    this.decode = decode;
    this.userDC = userDC;
  }

  // @PostMapping("resumes")

  @PostMapping(value = "resumes")
  @Operation(
    summary = "API Thêm Resume mới",
    description = "Returns a list of all resumes"
  )
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<CreateResumeAdminResponse> addResume(
    @Valid @RequestBody(required = false) CreateResumeAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    try {
      User user = userDC.findUserNameByToken(
        decode.AuthenticationDecode(authorizationHeader)
      );

      if (request == null) {
        request = CreateResumeAdminRequest.emptyResume();
      }

      Resume resume = resumeService.addResume(request, user, null);
      return ResponseEntity.ok(
        CreateResumeAdminResponse.builder().resume(resume).build()
      );
    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new RuntimeException("Error processing request: " + e.getMessage());
    }
  }

  @GetMapping("resumes/{id}")
  @LogExecutionTime
  public ResponseEntity<GetResumeAdminResponse> getResumeById(
    @PathVariable String id
  ) {
    Resume resume = resumeService.getResumeById(id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(GetResumeAdminResponse.builder().resume(resume).build());
  }

  // @GetMapping("resumes")
  // @LogExecutionTime
  // public ResponseEntity<ListResumeResponse> getResume() {
  //   List<Resume> resumes = resumeService.listResumes();
  //   return ResponseEntity
  //     .status(HttpStatus.OK)
  //     .body(ListResumeResponse.builder().resume(resumes).build());
  // }

  @GetMapping("resumes")
  public ResponseEntity<List<Resume>> getResumes() {
    List<Resume> resumes = resumeService.listResumes();
    return ResponseEntity.ok(resumes);
  }

  @PatchMapping("resumes/{id}")
  @Operation(
    summary = "API Update Resume Support For Auto Save ",
    description = "Update API Resume For Auto Save"
  )
  @LogExecutionTime
  public ResponseEntity<UpdateResumeAdminResponse> updateResume(
    @PathVariable String id,
    @Valid @RequestBody UpdateResumeAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
    // Removed MultipartFile parameter
  ) {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );
    if (request == null) {
      request = UpdateResumeAdminRequest.emptyResume();
    }
    Resume resume = resumeService.updateResume(id, request, user);

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(UpdateResumeAdminResponse.builder().resume(resume).build());
  }

  @PatchMapping("resumes-edit/{id}")
  @Operation(
    summary = "API Update Resume  ",
    description = "Update API Resume "
  )
  @LogExecutionTime
  public ResponseEntity<UpdateResumeAdminResponse> EditResume(
    @PathVariable String id,
    @Valid @RequestBody EditResumeAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
    // Removed MultipartFile parameter
  ) {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );
  
    Resume resume = resumeService.EditResume(id, request, user);

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(UpdateResumeAdminResponse.builder().resume(resume).build());
  }

  @DeleteMapping("resumes/{id}")
  public ResponseEntity<DeleteResumeResponse> deleteResume(
    @PathVariable String id
  ) {
    resumeService.deleteResume(id);
    return ResponseEntity.ok().build();
  }
}
