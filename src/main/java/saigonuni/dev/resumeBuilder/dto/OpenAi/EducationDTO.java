package saigonuni.dev.resumeBuilder.dto.OpenAi;
import lombok.Data;

@Data
public class EducationDTO {
    
    private String degree;
    private String school;
    private String startDate;
    private String endDate;

    // Getters and setters
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

    public String getStartDate() {
      return startDate;
    }

    public void setStartDate(String startDate) {
      this.startDate = startDate;
    }

    public String getEndDate() {
      return endDate;
    }

    public void setEndDate(String endDate) {
      this.endDate = endDate;
    }
}
