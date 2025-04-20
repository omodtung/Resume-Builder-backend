package saigonuni.dev.resumeBuilder.dto.resume;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.domain.dto.EducationDTO;
import saigonuni.dev.resumeBuilder.domain.dto.WorkExperienceDTO;

@Data
@Builder
public class UpdateResumeAdminRequest {

  private Long id;
  private String firstName;

  private String lastName;

  private String title;

  private String description;

  private String photoUrl = null;

  private String colorHex = "#000000"; // Default value is set to black

  private String borderStyle = "squircle"; // Default value is set to squircle

  private String summary;

  private String jobTitle;

  private String city;

  private String country;

  private String phone;

  private String email;

  private List<WorkExperienceDTO> workExperiences = new ArrayList<>();
  private List<EducationDTO> educations = new ArrayList<>();
  private List<String> skills = new ArrayList<>();

  public static UpdateResumeAdminRequest emptyResume() {
    return UpdateResumeAdminRequest
      .builder()
      .firstName("")
      .lastName("")
      .title("Untitled Resume")
      .description("")
      .photoUrl("")
      .colorHex("#000000")
      .borderStyle("squircle")
      .summary("")
      .jobTitle("")
      .city("")
      .country("")
      .phone("")
      .email("")
      .workExperiences(List.of())
      .educations(List.of())
      .skills(List.of())
      .build();
  }
}
