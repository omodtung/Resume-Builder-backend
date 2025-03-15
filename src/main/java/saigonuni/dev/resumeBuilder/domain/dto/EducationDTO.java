package saigonuni.dev.resumeBuilder.domain.dto;

import java.time.LocalDate;

public class EducationDTO {

  private String degree;
  private String school;
  private LocalDate startDate;
  private LocalDate endDate;

  // Getters and Setters
  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school = school;
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
}
