package saigonuni.dev.resumeBuilder.dto.Stripe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeRequest {

  private String priceId;
}
