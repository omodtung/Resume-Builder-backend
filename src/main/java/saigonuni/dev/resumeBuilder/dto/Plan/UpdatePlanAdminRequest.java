package saigonuni.dev.resumeBuilder.dto.Plan;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePlanAdminRequest {

  @NotBlank(message = "Plan name cannot be blank")
  private String plansName;

  private String Description;

  private String price;
}
