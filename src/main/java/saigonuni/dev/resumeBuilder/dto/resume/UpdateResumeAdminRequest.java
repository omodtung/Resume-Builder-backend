package saigonuni.dev.resumeBuilder.dto.resume;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UpdateResumeAdminRequest {
    

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  @NotEmpty
  private String title;

  @NotEmpty
  private String description;

  @NotEmpty(message = "Photo URL cannot be empty")
  private String photoUrl;

  @NotEmpty(message = "color Hex cannot empty")
  private String colorHex = "#000000"; // Default value is set to black

  @NotEmpty(message = "Border style cannot be empty")
  private String borderStyle = "squircle"; // Default value is set to squircle

  @Size(max = 500, message = "Summary should not exceed 500 characters")
  private String summary;

  @Size(max = 100, message = "Job title should not exceed 100 characters")
  private String jobTitle;

  @Size(max = 100, message = "City name should not exceed 100 characters")
  private String city;

  @Size(max = 100, message = "Country name should not exceed 100 characters")
  private String country;

  @Pattern(
    regexp = "^\\+?[0-9]{1,15}$",
    message = "Phone number should be a valid international number"
  )
  private String phone;

  @Email(message = "Email should be a valid email address")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  private List<WorkExperience> workExperiences = new ArrayList<>();
  private List<Education> educations = new ArrayList<>();
  private List<String> skills = new ArrayList<>();
}
