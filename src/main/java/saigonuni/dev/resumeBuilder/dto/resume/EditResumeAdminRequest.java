package saigonuni.dev.resumeBuilder.dto.resume;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.domain.dto.EducationDTO;
import saigonuni.dev.resumeBuilder.dto.Education.EducationResumeEdit;
import saigonuni.dev.resumeBuilder.dto.workExperience.workExperienceResumeEditRequestDTO;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class EditResumeAdminRequest {

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

  private List<workExperienceResumeEditRequestDTO> workExperiences = new ArrayList<>();
  private List<EducationResumeEdit> educations = new ArrayList<>();
  private List<String> skills = new ArrayList<>();
}
