package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
  name = "Resume Admin Controller",
  description = "Operations pertaining to admin management of resumes"
)
@RestController
@RequestMapping("/admin/resumes")
public class ResumeAdminController {

  @GetMapping
  @Operation(
    summary = "Get all resumes",
    description = "Returns a list of all resumes"
  )
  public String getAllResumes() {
    return "List of resumes";
  }
}
