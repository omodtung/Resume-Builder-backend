package saigonuni.dev.resumeBuilder.domain.dto;
import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ResumeDTO {

  private Long id;

  @NotNull(message = "Title cannot be null")
  private String title;

  private String description;
  private String photoUrl;

  @NotNull(message = "ColorHex cannot be null")
  private String colorHex = "#000000";

  @NotNull(message = "BorderStyle cannot be null")
  private String borderStyle = "squircle";

  private String summary;

  @Size(min = 3, message = "FirstName must have at least 3 characters")
  private String firstName;

  private String lastName;
  private String jobTitle;
  private String city;
  private String country;

  private String phone;

  private List<String> skills;

  @Email(
    message = "Invalid email format",
    regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
  )
  private String email;


  private List<WorkExperienceDTO> workExperiences;
  private List<EducationDTO> educations;

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getColorHex() {
    return colorHex;
  }

  public void setColorHex(String colorHex) {
    this.colorHex = colorHex;
  }

  public String getBorderStyle() {
    return borderStyle;
  }

  public void setBorderStyle(String borderStyle) {
    this.borderStyle = borderStyle;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  public List<String> getSkills() {
    return skills;
}

public void setSkills(List<String> skills) {
    this.skills = skills;
}
public List<WorkExperienceDTO> getWorkExperiences() {
    return workExperiences;
  }

  public void setWorkExperiences(List<WorkExperienceDTO> workExperiences) {
    this.workExperiences = workExperiences;
  }

  public List<EducationDTO> getEducations() {
    return educations;
  }

  public void setEducations(List<EducationDTO> educations) {
    this.educations = educations;
  }
}
