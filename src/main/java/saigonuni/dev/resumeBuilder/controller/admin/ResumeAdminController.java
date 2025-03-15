package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.domain.dto.ResumeDTO;
import saigonuni.dev.resumeBuilder.service.ResumeService;

@Tag(
  name = "Resume Admin Controller",
  description = "Operations pertaining to admin management of resumes"
)
@RestController
@RequestMapping("admin")
public class ResumeAdminController {

  private final ResumeService resumeService;

  @Autowired
  public ResumeAdminController(ResumeService resumeService) {
    this.resumeService = resumeService;
  }

  @GetMapping("test")
  @Operation(
    summary = "Get all resumes",
    description = "Returns a list of all resumes"
  )
  public String getAllResumes() {
    return "List of resumes";
  }

  // API Thêm Resume mới
  @PostMapping("resumes")
  @Operation(
    summary = "API Thêm Resume mới",
    description = "Returns a list of all resumes"
  )
  public ResponseEntity<Resume> addResume(@RequestBody Resume resume) {
    try {
      Resume newResume = resumeService.addResume(resume);
      return new ResponseEntity<>(newResume, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
