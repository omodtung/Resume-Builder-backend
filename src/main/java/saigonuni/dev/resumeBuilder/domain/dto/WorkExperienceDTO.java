package saigonuni.dev.resumeBuilder.domain.dto;

import java.time.LocalDate;
import saigonuni.dev.resumeBuilder.domain.Resume;

public class  WorkExperienceDTO {

  private String position;
  private String company;
  private LocalDate startDate;
  private LocalDate endDate;
  private String description;
  public Resume resume;

  public Resume getResume() {
    return resume;
  }

  public void setResume(Resume resume) {
    this.resume = resume;
  }

  // Getters and Setters
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
