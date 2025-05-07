package saigonuni.dev.resumeBuilder.dto.Plan;

import lombok.Data;

@Data
public class PlanSubscription {
  private Long id;
  private String plansName;
  private String Description;
  private String price;

  public Long getId() {
    return id;
  }

  public String getPlansName() {
    return plansName;
  }

  public String getDescription() {
    return Description;
  }

  public String getPrice() {
    return price;
  }
}
