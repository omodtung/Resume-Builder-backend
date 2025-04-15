package saigonuni.dev.resumeBuilder.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.Stripe.StripeResponse;

@Service
public class StripeService {

  @Value("${stripe.secretKey}")
  private String secretKey;

  public StripeResponse createCheckoutSession(String priceId, User user)
    throws StripeException {
    Stripe.apiKey = secretKey;

    SessionCreateParams params = SessionCreateParams
      .builder()
      .addLineItem(
        SessionCreateParams.LineItem
          .builder()
          .setPrice(priceId)
          .setQuantity(1L)
          .build()
      )
      .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
      .setSuccessUrl("http://localhost:8080/billing/success") // Replace with your success URL
      .setCancelUrl("http://localhost:8080/billing") // Replace with your cancel URL
      .setCustomerEmail(user.getEmail())
      .setSubscriptionData(
        SessionCreateParams.SubscriptionData
          .builder()
          .putMetadata("userId", String.valueOf(user.getId()))
          .build()
      )
      .build();

    Session session = Session.create(params);

    return StripeResponse
      .builder()
      .status("SUCCESS")
      .message("Payment session created ")
      .sessionId(session.getId())
      .sessionUrl(session.getUrl())
      .build();
  }
}
