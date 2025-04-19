package saigonuni.dev.resumeBuilder.webhook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session; // Import Session
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import saigonuni.dev.resumeBuilder.domain.Plan; // Import Plan
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.repository.PlanRepository; // Import PlanRepository
import saigonuni.dev.resumeBuilder.repository.UserRepository;
import saigonuni.dev.resumeBuilder.repository.UserSubscriptionRepository;

@Slf4j
@RestController
@RequestMapping("api/stripe-webhook")
@RequiredArgsConstructor
public class stripeRoute {

  @Value("${STRIPE_WEBHOOK_SECRET}")
  private String endpointSecret;

  @Autowired
  private final UserSubscriptionRepository userSubscriptionRepository;

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final PlanRepository planRepository; // Inject PlanRepository

  public stripeRoute() {
    this.userSubscriptionRepository = null;
    this.userRepository = null;
    this.planRepository = null;
  }

  @PostMapping
  public ResponseEntity<String> handleStripeWebhook(
    @RequestBody String payload,
    HttpServletRequest request
  ) {
    String sigHeader = request.getHeader("Stripe-Signature");
    Event event = null;

    try {
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (SignatureVerificationException e) {
      log.warn("Webhook error while validating signature.", e);
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Invalid signature");
    } catch (Exception e) {
      log.error("Webhook error.", e);
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Webhook error");
    }

    log.info("Received event: id={}, type={}", event.getId(), event.getType());

    StripeObject stripeObject = event
      .getDataObjectDeserializer()
      .getObject()
      .orElse(null);

    if (stripeObject == null) {
      log.error(
        "Failed to deserialize Stripe object for event id={}",
        event.getId()
      );
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Internal server error");
    }

    try {
      switch (event.getType()) {
        case "checkout.session.completed":
          handleSessionCompleted((Session) stripeObject);
          break;
        case "customer.subscription.created":
        case "customer.subscription.updated":
          handleSubscriptionCreatedOrUpdated((Subscription) stripeObject);
          break;
        case "customer.subscription.deleted":
          handleSubscriptionDeleted((Subscription) stripeObject);
          break;
        default:
          log.info("Unhandled event type: {}", event.getType());
          break;
      }
      return ResponseEntity.ok("Event received");
    } catch (StripeException e) {
      log.error(
        "Stripe API error processing event id={}: {}",
        event.getId(),
        e.getMessage(),
        e
      );
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Stripe API error");
    } catch (Exception e) {
      log.error(
        "Error processing event id={}: {}",
        event.getId(),
        e.getMessage(),
        e
      );
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Internal server error");
    }
  }

  @Transactional
  void handleSessionCompleted(Session session) {
    Map<String, String> metadata = session.getMetadata();
    String userIdStr = metadata != null ? metadata.get("userId") : null;
    String customerId = session.getCustomer();

    if (userIdStr == null || customerId == null) {
      log.error(
        "User ID or Customer ID is missing in session metadata for session id={}",
        session.getId()
      );
      // Consider throwing an exception or handling this case appropriately
      return;
    }

    try {
      // Primarily logging here. UserSubscription links customerId to user.
      // If you need to store stripeCustomerId directly on the User entity,
      // uncomment and adjust the logic below.
      log.info(
        "Checkout session completed for customerId={}, potential userId={}",
        customerId,
        userIdStr
      );
      /*
            try {
                Long userId = Long.parseLong(userIdStr);
                Optional<User> userOptional = userRepository.findById(userId);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    // Assuming User entity has a field 'stripeCustomerId'
                    // user.setStripeCustomerId(customerId);
                    // userRepository.save(user);
                    log.info("Updated user id={} with stripeCustomerId={}", userId, customerId);
                } else {
                    log.warn("User not found for id={} during session completion handling.", userId);
                }
            } catch (NumberFormatException e) {
                log.error("Invalid userId format '{}' in session metadata for session id={}", userIdStr, session.getId());
            }
            */
    } catch (Exception e) { // Catch broader exceptions during lookup if needed
      log.error(
        "Error processing session completed for session id={}: {}",
        session.getId(),
        e.getMessage(),
        e
      );
    }
  }

  @Transactional
  void handleSubscriptionCreatedOrUpdated(Subscription subscription)
    throws StripeException {
    String customerId = subscription.getCustomer();
    Map<String, String> metadata = subscription.getMetadata();
    String userIdStr = metadata != null ? metadata.get("userId") : null;

    if (userIdStr == null) {
      log.error(
        "User ID is missing in subscription metadata for subscription id={}",
        subscription.getId()
      );
      // Consider throwing an exception or handling this case appropriately
      return;
    }

    Long userId;
    try {
      userId = Long.parseLong(userIdStr);
    } catch (NumberFormatException e) {
      log.error(
        "Invalid userId format '{}' in subscription metadata for subscription id={}",
        userIdStr,
        subscription.getId()
      );
      return;
    }

    // Fetch the associated User
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty()) {
      log.error(
        "User not found for userId={} from subscription metadata for subscription id={}",
        userId,
        subscription.getId()
      );
      return; // Or throw an exception
    }
    User user = userOptional.get();

    // Fetch the associated Plan (using stripePriceId)
    String stripePriceId = null;
    if (!subscription.getItems().getData().isEmpty()) {
      stripePriceId =
        subscription.getItems().getData().get(0).getPrice().getId();
    } else {
      log.warn(
        "Subscription {} has no items, cannot determine stripePriceId.",
        subscription.getId()
      );
      // Decide how to handle this - maybe skip update or throw error?
      return;
    }

    Optional<Plan> planOptional = planRepository.findByStripePriceId(
      stripePriceId
    ); // Assuming PlanRepository has this method
    if (planOptional.isEmpty()) {
      log.error(
        "Plan not found for stripePriceId={} from subscription id={}",
        stripePriceId,
        subscription.getId()
      );
      return; // Or throw an exception
    }
    Plan plan = planOptional.get();

    String status = subscription.getStatus();
    boolean isActive =
      "active".equals(status) ||
      "trialing".equals(status) ||
      "past_due".equals(status);

    if (isActive) {
      // Find existing subscription by userId OR stripeSubscriptionId for upsert logic
      Optional<UserSubscription> existingSubscriptionOpt = userSubscriptionRepository.findByStripeSubscriptionId(
        subscription.getId()
      );
      if (existingSubscriptionOpt.isEmpty()) {
        existingSubscriptionOpt =
          userSubscriptionRepository.findByUserId(userId); // Check by userId if not found by subscriptionId
      }

      UserSubscription userSubscription = existingSubscriptionOpt.orElse(
        new UserSubscription()
      );

      userSubscription.setUser(user); // Set User object
      userSubscription.setPlan(plan); // Set Plan object
      userSubscription.setStripeSubscriptionId(subscription.getId());
      userSubscription.setStripeCustomerId(customerId);
      userSubscription.setStripeCurrentPeriodEnd(
        LocalDateTime.ofInstant(
          Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()),
          ZoneId.systemDefault()
        )
      );
      userSubscription.setStripeCancelAtPeriodEnd(
        subscription.getCancelAtPeriodEnd()
      );
      // Assuming UserSubscription has BaseEntity fields like createdAt, updatedAt
      // These might be handled automatically by JPA Auditing if configured

      userSubscriptionRepository.save(userSubscription);
      log.info(
        "Upserted UserSubscription for userId={}, subscriptionId={}",
        userId,
        subscription.getId()
      );
    } else {
      // Status is not active, trialing, or past_due (e.g., canceled, incomplete)
      // The original code deletes based on customerId, which might affect multiple subscriptions if a customer resubscribes.
      // Deleting based on subscriptionId might be safer if you only want to remove this specific inactive subscription record.
      // Let's stick to the original logic for now (delete by customerId).
      userSubscriptionRepository.deleteByStripeCustomerId(customerId);
      log.info(
        "Deleted UserSubscription(s) for customerId={} due to inactive status ({}) for subscriptionId={}",
        customerId,
        status,
        subscription.getId()
      );
    }
  }

  @Transactional
  void handleSubscriptionDeleted(Subscription subscription) {
    String customerId = subscription.getCustomer();
    // The original code deletes based on customerId.
    userSubscriptionRepository.deleteByStripeCustomerId(customerId);
    log.info(
      "Deleted UserSubscription(s) for customerId={} due to subscription deletion event for subscriptionId={}",
      customerId,
      subscription.getId()
    );
  }
}
