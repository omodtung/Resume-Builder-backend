package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.GetResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.ListResumeResponse;
import saigonuni.dev.resumeBuilder.service.ResumeService;

@Tag(
  name = "Resume Admin Controller",
  description = "Operations pertaining to admin management of resumes"
)
@RestController
@RequestMapping("admin")
public class ResumeAdminController extends BaseController {

  private final ResumeService resumeService;

  @Autowired
  public ResumeAdminController(ResumeService resumeService) {
    this.resumeService = resumeService;
  }

  @PostMapping("resumes")
  @Operation(
    summary = "API Thêm Resume mới",
    description = "Returns a list of all resumes"
  )
  public ResponseEntity<CreateResumeAdminResponse> addResume(
    @Valid @RequestBody CreateResumeAdminRequest request
  ) {
    System.out.println("Testing input Data" + request);
    Resume resume = resumeService.addResume(request);
    return ResponseEntity.ok(
      CreateResumeAdminResponse.builder().resume(resume).build()
    );
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

  @GetMapping("resumes")
  @LogExecutionTime
  public ResponseEntity<ListResumeResponse> getResume() {
    List<Resume> resumes = resumeService.listResumes();
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(ListResumeResponse.builder().resume(resumes).build());
  }
}
