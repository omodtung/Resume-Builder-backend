package saigonuni.dev.resumeBuilder.dto.UserSubscription;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import saigonuni.dev.resumeBuilder.dto.Plan.PlanSubscription;
import saigonuni.dev.resumeBuilder.dto.User.UserSubDTO;

@Data
@Builder
public class UserSupcriptionDTO {

  private Long id;
  private String stripeCustomerId;
  private String stripeSubscriptionId;
  private LocalDateTime stripeCurrentPeriodEnd;
  private Boolean stripeCancelAtPeriodEnd;
  private UserSubDTO user;
  private PlanSubscription plan;

  public Long getId() {
    return id;
  }

  public String getStripeCustomerId() {
    return stripeCustomerId;
  }

  public String getStripeSubscriptionId() {
    return stripeSubscriptionId;
  }

  public LocalDateTime getStripeCurrentPeriodEnd() {
    return stripeCurrentPeriodEnd;
  }

  public Boolean getStripeCancelAtPeriodEnd() {
    return stripeCancelAtPeriodEnd;
  }

  public UserSubDTO getUser() {
    return user;
  }

  public PlanSubscription getPlan() {
    return plan;
  }
}
