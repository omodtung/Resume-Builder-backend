package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping("resume")
  @Operation(
    summary = "Create a new resume in admin site",
    description = "Create a new resume in admin site for user use"
  )
  public ResponseEntity<Resume> createResume(@RequestBody ResumeDTO resumeDTO) {
    List<WorkExperience> workExperiences = resumeDTO.getWorkExperiences() !=
      null
      ? resumeDTO
        .getWorkExperiences()
        .stream()
        .map(dto ->
          new WorkExperience(
            dto.getPosition(),
            dto.getCompany(),
            dto.getStartDate(),
            dto.getEndDate(),
            dto.getDescription()
          )
        )
        .collect(Collectors.toList())
      : null;

    List<Education> educations = resumeDTO.getEducations() != null
      ? resumeDTO
        .getEducations()
        .stream()
        .map(dto ->
          new Education(
            dto.getDegree(),
            dto.getSchool(),
            dto.getStartDate(),
            dto.getEndDate()
          )
        )
        .collect(Collectors.toList())
      : null;

    User user = new User(); // Set user appropriately

    Resume resume = new Resume(
      null,
      resumeDTO.getTitle(),
      resumeDTO.getDescription(),
      resumeDTO.getPhotoUrl(),
      resumeDTO.getColorHex(),
      resumeDTO.getBorderStyle(),
      resumeDTO.getSummary(),
      resumeDTO.getFirstName(),
      resumeDTO.getLastName(),
      resumeDTO.getJobTitle(),
      resumeDTO.getCity(),
      resumeDTO.getCountry(),
      resumeDTO.getPhone(),
      resumeDTO.getEmail(),
      user,
      workExperiences,
      educations,
      resumeDTO.getSkills()
    );

    // Associate educations with the resume
    if (educations != null) {
      for (Education education : educations) {
        education.setResume(resume);
      }
    }

    Resume createdResume = resumeService.handleSaveResume(resume);
    return ResponseEntity.ok(createdResume);
  }
}
