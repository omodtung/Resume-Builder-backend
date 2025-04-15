package saigonuni.dev.resumeBuilder.dto.Stripe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.dto.Stripe.StripeRequest;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeResponse {
    
    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;
}
