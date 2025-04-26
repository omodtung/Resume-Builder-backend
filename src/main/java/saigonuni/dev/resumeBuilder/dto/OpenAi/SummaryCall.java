package saigonuni.dev.resumeBuilder.dto.OpenAi;

import lombok.Data;

@Data
public class SummaryCall {

  private String jobTitle;
  // private List<WorkExperience> workExperiences;
  // private List<Education> educations;
  private String skills;

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getSkills() {
    return skills;
  }

  public void setSkills(String skills) {
    this.skills = skills;
  }
}
