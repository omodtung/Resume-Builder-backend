package saigonuni.dev.resumeBuilder.webhook;

import com.stripe.Stripe; // *** THÊM IMPORT NÀY ***
import com.stripe.Stripe; // *** THÊM IMPORT NÀY ***
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.apps.Secret;
import com.stripe.model.checkout.Session; // Import Session
import com.stripe.model.checkout.Session; // Import Session
import com.stripe.net.Webhook;
import jakarta.annotation.PostConstruct; // *** THÊM IMPORT NÀY ***
import jakarta.annotation.PostConstruct; // *** THÊM IMPORT NÀY ***
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException; // Add this import
import java.io.IOException; // Add this import
import java.nio.charset.StandardCharsets; // Add this import
import java.nio.charset.StandardCharsets; // Add this import
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor; // Sử dụng @RequiredArgsConstructor cho final fields
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils; // Add this import
import org.springframework.util.StreamUtils; // Add this import
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

  //   // -------------------------------------------------------------------------------------------------------------
  // //   @Value("${stripe.webhook.secret}") // Inject from properties
  //   private String endpointSecret = "whsec_fVqd42xr6WDkutkorecqZMsHwJtBcZXU";

  //   // Use final for required dependencies with @RequiredArgsConstructor
  //   @Autowired
  //   private final UserSubscriptionRepository userSubscriptionRepository;

  //   @Autowired
  //   private final UserRepository userRepository;

  //   @Autowired
  //   private final PlanRepository planRepository;

  //   // No explicit constructor or @Autowired needed

  //   @PostMapping // Removed consumes = "application/json" as we read raw body
  //   public ResponseEntity<String> handleStripeWebhook(
  //     HttpServletRequest request
  //   ) {
  //     String payload;
  //     try {
  //       payload =
  //         StreamUtils.copyToString(
  //           request.getInputStream(),
  //           StandardCharsets.UTF_8
  //         );
  //     } catch (IOException e) {
  //       log.error("Failed to read webhook request body", e);
  //       return ResponseEntity
  //         .status(HttpStatus.INTERNAL_SERVER_ERROR)
  //         .body("Failed to read request body");
  //     }

  //     String sigHeader = request.getHeader("Stripe-Signature");
  //     Event event; // Declare outside try

  //     // Verify secret is loaded
  //     if (
  //       endpointSecret == null ||
  //       endpointSecret.isBlank() ||
  //       !endpointSecret.startsWith("whsec_")
  //     ) {
  //       log.error("Stripe webhook secret is not configured correctly!");
  //       // Don't reveal secret details in the response
  //       return ResponseEntity
  //         .status(HttpStatus.INTERNAL_SERVER_ERROR)
  //         .body("Webhook processor configuration error.");
  //     }

  //     try {
  //       event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
  //     } catch (SignatureVerificationException e) {
  //       log.warn("Webhook signature verification failed.", e);
  //       return ResponseEntity
  //         .status(HttpStatus.BAD_REQUEST)
  //         .body("Invalid signature");
  //     } catch (Exception e) { // Catch broader exceptions during construction if needed
  //       log.error("Error constructing Stripe event.", e);
  //       return ResponseEntity
  //         .status(HttpStatus.BAD_REQUEST)
  //         .body("Webhook error"); // Or 500?
  //     }

  //     log.info(
  //       "Received Stripe event: Id={}, Type={}",
  //       event.getId(),
  //       event.getType()
  //     );

  //     // --- Deserialization ---
  //     EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
  //     StripeObject stripeObject = null;

  //     if (
  //       dataObjectDeserializer != null &&
  //       dataObjectDeserializer.getObject().isPresent()
  //     ) {
  //       stripeObject = dataObjectDeserializer.getObject().get();
  //     } else {
  //       log.error(
  //         "Failed to deserialize Stripe object for event id={}. Raw data: {}",
  //         event.getId(),
  //         event.getData() != null ? event.getData().toJson() : "null"
  //       );
  //       // Acknowledge receipt but indicate processing error
  //       return ResponseEntity
  //         .status(HttpStatus.INTERNAL_SERVER_ERROR)
  //         .body("Internal error during event processing");
  //     }

  //     // --- Event Handling ---
  //     try {
  //       switch (event.getType()) {
  //         case "checkout.session.completed":
  //           if (stripeObject instanceof Session) {
  //             handleSessionCompleted((Session) stripeObject);
  //           } else {
  //             log.warn(
  //               "Expected Session object for checkout.session.completed, but got: {}",
  //               stripeObject.getClass().getName()
  //             );
  //           }
  //           break;
  //         case "customer.subscription.created":
  //         case "customer.subscription.updated":
  //           if (stripeObject instanceof Subscription) {
  //             handleSubscriptionCreatedOrUpdated((Subscription) stripeObject);
  //           } else {
  //             log.warn(
  //               "Expected Subscription object for customer.subscription.created/updated, but got: {}",
  //               stripeObject.getClass().getName()
  //             );
  //           }
  //           break;
  //         case "customer.subscription.deleted":
  //           if (stripeObject instanceof Subscription) {
  //             // handleSubscriptionDeleted((Subscription) stripeObject);
  //           } else {
  //             log.warn(
  //               "Expected Subscription object for customer.subscription.deleted, but got: {}",
  //               stripeObject.getClass().getName()
  //             );
  //           }
  //           break;
  //         default:
  //           log.info("Unhandled event type: {}", event.getType());
  //           break;
  //       }
  //       return ResponseEntity.ok("Event received");
  //     } catch (StripeException e) {
  //       log.error(
  //         "Stripe API error processing event id={}: {}",
  //         event.getId(),
  //         e.getMessage(),
  //         e
  //       );
  //       return ResponseEntity
  //         .status(HttpStatus.INTERNAL_SERVER_ERROR)
  //         .body("Stripe API error");
  //     } catch (Exception e) { // Catch application-specific errors during processing
  //       log.error(
  //         "Application error processing event id={}: {}",
  //         event.getId(),
  //         e.getMessage(),
  //         e
  //       );
  //       return ResponseEntity
  //         .status(HttpStatus.INTERNAL_SERVER_ERROR)
  //         .body("Internal server error");
  //     }
  //   }

  //   @Transactional
  //   void handleSessionCompleted(Session session) {
  //     Map<String, String> metadata = session.getMetadata();
  //     String userIdStr = metadata != null ? metadata.get("userId") : null;
  //     String customerId = session.getCustomer();

  //     if (userIdStr == null || customerId == null) {
  //       log.error(
  //         "User ID or Customer ID is missing in session metadata for session id={}",
  //         session.getId()
  //       );
  //       return; // Or throw exception if this is critical
  //     }

  //     log.info(
  //       "Processing checkout.session.completed for customerId={}, potential userId={}",
  //       customerId,
  //       userIdStr
  //     );
  //     // If you need to update User entity:
  //     /*
  //         try {
  //             Long userId = Long.parseLong(userIdStr);
  //             Optional<User> userOptional = userRepository.findById(userId);

  //             if (userOptional.isPresent()) {
  //                 User user = userOptional.get();
  //                 // Assuming User entity has a field 'stripeCustomerId'
  //                 // user.setStripeCustomerId(customerId);
  //                 // userRepository.save(user);
  //                 log.info("Updated user id={} with stripeCustomerId={}", userId, customerId);
  //             } else {
  //                 log.warn("User not found for id={} during session completion handling.", userId);
  //             }
  //         } catch (NumberFormatException e) {
  //             log.error("Invalid userId format '{}' in session metadata for session id={}", userIdStr, session.getId());
  //         } catch (Exception e) {
  //             log.error("Error processing session completed for session id={}: {}", session.getId(), e.getMessage(), e);
  //             // Potentially re-throw or handle differently
  //         }
  //         */
  //   }

  //   @Transactional
  //   void handleSubscriptionCreatedOrUpdated(Subscription subscription)
  //     throws StripeException {
  //     String customerId = subscription.getCustomer();
  //     Map<String, String> metadata = subscription.getMetadata();
  //     String userIdStr = metadata != null ? metadata.get("userId") : null;
  //     String subscriptionId = subscription.getId(); // Get subscription ID

  //     if (userIdStr == null) {
  //       log.error(
  //         "User ID is missing in subscription metadata for subscription id={}",
  //         subscriptionId
  //       );
  //       return;
  //     }

  //     Long userId;
  //     try {
  //       userId = Long.parseLong(userIdStr);
  //     } catch (NumberFormatException e) {
  //       log.error(
  //         "Invalid userId format '{}' in subscription metadata for subscription id={}",
  //         userIdStr,
  //         subscriptionId
  //       );
  //       return;
  //     }

  //     Optional<User> userOptional = userRepository.findById(userId);
  //     if (userOptional.isEmpty()) {
  //       log.error(
  //         "User not found for userId={} from subscription metadata for subscription id={}",
  //         userId,
  //         subscriptionId
  //       );
  //       return;
  //     }
  //     User user = userOptional.get();

  //     String stripePriceId = null;
  //     if (!subscription.getItems().getData().isEmpty()) {
  //       stripePriceId =
  //         subscription.getItems().getData().get(0).getPrice().getId();
  //     } else {
  //       log.warn(
  //         "Subscription {} has no items, cannot determine stripePriceId.",
  //         subscriptionId
  //       );
  //       return;
  //     }

  //     Optional<Plan> planOptional = planRepository.findByStripePriceId(
  //       stripePriceId
  //     );
  //     if (planOptional.isEmpty()) {
  //       log.error(
  //         "Plan not found for stripePriceId={} from subscription id={}",
  //         stripePriceId,
  //         subscriptionId
  //       );
  //       return;
  //     }
  //     Plan plan = planOptional.get();

  //     String status = subscription.getStatus();
  //     boolean isActive =
  //       "active".equals(status) ||
  //       "trialing".equals(status) ||
  //       "past_due".equals(status);

  //     if (isActive) {
  //       Optional<UserSubscription> existingSubscriptionOpt = userSubscriptionRepository.findByStripeSubscriptionId(
  //         subscriptionId
  //       );
  //       // Optional: If not found by sub id, you *could* check by userId, but might lead to overwriting if multiple subs were possible
  //       // if (existingSubscriptionOpt.isEmpty()) {
  //       //    existingSubscriptionOpt = userSubscriptionRepository.findByUserId(userId);
  //       // }

  //       UserSubscription userSubscription = existingSubscriptionOpt.orElse(
  //         new UserSubscription()
  //       );

  //       userSubscription.setUser(user);
  //       userSubscription.setPlan(plan);
  //       userSubscription.setStripeSubscriptionId(subscriptionId);
  //       userSubscription.setStripeCustomerId(customerId);
  //       userSubscription.setStripeCurrentPeriodEnd(
  //         LocalDateTime.ofInstant(
  //           Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()),
  //           ZoneId.systemDefault()
  //         )
  //       );
  //       userSubscription.setStripeCancelAtPeriodEnd(
  //         subscription.getCancelAtPeriodEnd() != null &&
  //         subscription.getCancelAtPeriodEnd()
  //       ); // Handle null boolean

  //       userSubscriptionRepository.save(userSubscription);
  //       log.info(
  //         "Upserted UserSubscription for userId={}, subscriptionId={}",
  //         userId,
  //         subscriptionId
  //       );
  //     } else {
  //       // Safer: Delete by subscription ID for inactive status
  //       // int deletedCount = userSubscriptionRepository.deleteByStripeSubscriptionId(subscriptionId);
  //       // log.info("Deleted {} UserSubscription(s) for subscriptionId={} due to inactive status ({})", deletedCount, subscriptionId, status);
  //     }
  //   }
  //   // @Transactional
  //   // void handleSubscriptionDeleted(Subscription subscription) {
  //   //     String subscriptionId = subscription.getId();
  //   //     // Safer: Delete by subscription ID
  //   //     int deletedCount = userSubscriptionRepository.deleteByStripeSubscriptionId(subscriptionId);
  //   //     log.info("Deleted {} UserSubscription(s) for subscriptionId={} due to subscription deletion event", deletedCount, subscriptionId);
  //   // }

  // ----------------------------------------------------------------------------------------------------------------

  // @Value("${stripe.api.key}")
  private String stripeApiKey =
    "sk_test_51QuBaRK6dhIc7YOm3TJxxTw409Wxuzkk6Mis7LVPEuitjglkIn3zChdiE6jweMVW7Mn4Na8WCTLJPymFvbL7mh4v00azltuaWj";

  // @Value("${stripe.webhook.secret}")
  private String endpointSecret = "whsec_fVqd42xr6WDkutkorecqZMsHwJtBcZXU"; // Giữ lại tên này hoặc đổi thành webhookSecret

  // Sử dụng final và để @RequiredArgsConstructor inject
  private final UserSubscriptionRepository userSubscriptionRepository;
  private final UserRepository userRepository;
  private final PlanRepository planRepository;

  // Khởi tạo Stripe API Key khi ứng dụng khởi động
  @PostConstruct
  public void initStripe() {
    if (stripeApiKey == null || stripeApiKey.isBlank()) {
      log.error("Stripe API key is not configured!");
      // Có thể throw exception ở đây để ngăn ứng dụng khởi động nếu cần
    } else {
      Stripe.apiKey = stripeApiKey;
      log.info("Stripe API Key initialized.");
    }

    // Kiểm tra webhook secret luôn ở đây
    if (
      endpointSecret == null ||
      endpointSecret.isBlank() ||
      !endpointSecret.startsWith("whsec_")
    ) {
      log.error("Stripe webhook secret is not configured correctly!");
      // Có thể throw exception
    } else {
      log.info("Stripe Webhook Secret loaded.");
    }
  }

  @PostMapping
  public ResponseEntity<String> handleStripeWebhook(
    HttpServletRequest request
  ) {
    String payload;
    try {
      payload =
        StreamUtils.copyToString(
          request.getInputStream(),
          StandardCharsets.UTF_8
        );
    } catch (IOException e) {
      log.error("Failed to read webhook request body", e);
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Failed to read request body");
    }

    String sigHeader = request.getHeader("Stripe-Signature");
    Event event;

    // Kiểm tra lại secret (mặc dù đã kiểm tra ở @PostConstruct)
    if (endpointSecret == null || endpointSecret.isBlank()) {
      log.error("Stripe webhook secret is missing during request processing!");
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Webhook processor configuration error.");
    }

    try {
      // Xác thực chữ ký webhook
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (SignatureVerificationException e) {
      log.warn("Webhook signature verification failed.", e);
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Invalid signature");
    } catch (Exception e) {
      log.error("Error constructing Stripe event from payload.", e);
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Webhook error during event construction");
    }

    log.info(
      "Received Stripe event: Id={}, Type={}, API Version={}",
      event.getId(),
      event.getType(),
      event.getApiVersion()
    ); // Log thêm API version

    // --- Deserialization ---
    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
    StripeObject stripeObject = null;

    if (dataObjectDeserializer.getObject().isPresent()) {
      stripeObject = dataObjectDeserializer.getObject().get();
      log.debug(
        "Successfully deserialized Stripe object: {}",
        stripeObject.toJson()
      ); // Log object nếu thành công (cẩn thận với dữ liệu nhạy cảm)
    } else {
      // Lỗi deserialization xảy ra ở đây!
      log.error(
        "Failed to deserialize Stripe object for event id={}. Type={}. Raw data: {}",
        event.getId(),
        event.getType(),
        event.getData() != null ? event.getData().toJson() : "null"
      );
      // Nguyên nhân có thể là do API Key chưa set hoặc version không khớp
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Internal error during event deserialization");
    }

    // --- Event Handling ---
    try {
      switch (event.getType()) {
        case "checkout.session.completed":
          // Kiểm tra kiểu tường minh hơn
          if (stripeObject instanceof Session) {
            handleSessionCompleted((Session) stripeObject);
          } else {
            log.warn(
              "Expected Session object for checkout.session.completed (Event ID: {}), but got: {}. Raw object: {}",
              event.getId(),
              stripeObject.getClass().getName(),
              stripeObject.toJson() // Log raw object để debug
            );
          }
          break;
        case "customer.subscription.created":
        case "customer.subscription.updated":
          if (stripeObject instanceof Subscription) {
            handleSubscriptionCreatedOrUpdated((Subscription) stripeObject);
          } else {
            log.warn(
              "Expected Subscription object for customer.subscription.created/updated (Event ID: {}), but got: {}. Raw object: {}",
              event.getId(),
              stripeObject.getClass().getName(),
              stripeObject.toJson()
            );
          }
          break;
        case "customer.subscription.deleted":
          if (stripeObject instanceof Subscription) {
            // handleSubscriptionDeleted((Subscription) stripeObject); // Bỏ comment nếu cần xử lý
            log.info(
              "Handling customer.subscription.deleted for sub_id: {}",
              ((Subscription) stripeObject).getId()
            );
          } else {
            log.warn(
              "Expected Subscription object for customer.subscription.deleted (Event ID: {}), but got: {}. Raw object: {}",
              event.getId(),
              stripeObject.getClass().getName(),
              stripeObject.toJson()
            );
          }
          break;
        // Thêm các case khác nếu cần
        // case "invoice.paid":
        // case "invoice.payment_failed":

        default:
          log.info("Unhandled event type: {}", event.getType());
          break;
      }
      // Trả về 200 OK cho Stripe để xác nhận đã nhận event
      return ResponseEntity.ok("Event received");
    } catch (StripeException e) {
      log.error(
        "Stripe API error processing event id={}: Status={}, Code={}, Message={}",
        event.getId(),
        e.getStatusCode(),
        e.getCode(),
        e.getMessage(),
        e
      );
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Stripe API error during processing");
    } catch (Exception e) { // Bắt lỗi ứng dụng trong quá trình xử lý logic
      log.error(
        "Application error processing event id={}: {}",
        event.getId(),
        e.getMessage(),
        e
      );
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Internal server error during processing");
    }
  }

  @Transactional
  void handleSessionCompleted(Session session) {
    Map<String, String> metadata = session.getMetadata();
    // Kiểm tra metadata null hoặc thiếu userId CẨN THẬN
    if (
      metadata == null ||
      metadata.get("userId") == null ||
      metadata.get("userId").isBlank()
    ) {
      log.error(
        "User ID is missing or empty in session metadata for session id={}. Metadata: {}",
        session.getId(),
        metadata // Log metadata để xem có gì
      );
      // Quan trọng: Đây là lỗi logic từ phía tạo Checkout Session
      // Bạn cần đảm bảo userId được đặt vào metadata khi tạo Session
      return; // Không xử lý tiếp nếu thiếu thông tin quan trọng
    }

    String userIdStr = metadata.get("userId");
    String customerId = session.getCustomer(); // session.getCustomer() có thể trả về String customer ID

    if (customerId == null) {
      log.error(
        "Customer ID is missing in session object for session id={}",
        session.getId()
      );
      return;
    }

    log.info(
      "Processing checkout.session.completed for customerId={}, userId={}",
      customerId,
      userIdStr
    );

    // Logic cập nhật User với customerId (nếu cần) - đã comment trong code gốc
    // Bạn nên đảm bảo logic này đúng và xử lý NumberFormatException
    try {
      Long userId = Long.parseLong(userIdStr);
      Optional<User> userOptional = userRepository.findById(userId);

      if (userOptional.isPresent()) {
        User user = userOptional.get();
        // Ví dụ: Cập nhật stripeCustomerId cho User nếu chưa có
        // if (user.getStripeCustomerId() == null) {
        //     user.setStripeCustomerId(customerId);
        //     userRepository.save(user);
        //     log.info("Associated user id={} with stripeCustomerId={}", userId, customerId);
        // } else if (!user.getStripeCustomerId().equals(customerId)) {
        //     log.warn("User id={} already has a different stripeCustomerId ({}). Received customerId={} from session {}",
        //              userId, user.getStripeCustomerId(), customerId, session.getId());
        // }
      } else {
        log.warn(
          "User not found for id={} during session completion handling (session id={}).",
          userId,
          session.getId()
        );
      }
    } catch (NumberFormatException e) {
      log.error(
        "Invalid userId format '{}' in session metadata for session id={}",
        userIdStr,
        session.getId()
      );
    } catch (Exception e) {
      log.error(
        "Error processing session completed logic for session id={}: {}",
        session.getId(),
        e.getMessage(),
        e
      );
      // Xem xét có nên throw để rollback transaction không
    }

    // Quan trọng: Sự kiện checkout.session.completed thường chỉ báo thanh toán thành công.
    // Việc tạo/cập nhật UserSubscription thường được xử lý bởi sự kiện
    // customer.subscription.created hoặc customer.subscription.updated (nếu mode là 'subscription')
    // hoặc invoice.paid (cho one-time payment hoặc subscription).
    // Nếu mode=subscription, session này sẽ dẫn đến việc tạo Subscription, và webhook subscription sẽ được gửi ngay sau đó.
    log.info(
      "Checkout session {} completed. Waiting for subscription/invoice events if applicable.",
      session.getId()
    );
  }

  @Transactional
  void handleSubscriptionCreatedOrUpdated(Subscription subscription)
    throws StripeException {
    String customerId = subscription.getCustomer();
    Map<String, String> metadata = subscription.getMetadata(); // Metadata của Subscription
    String subscriptionId = subscription.getId();

    // Lấy userId từ metadata của Subscription (quan trọng)
    // Bạn cần đảm bảo userId được thêm vào metadata KHI TẠO Subscription
    // Nếu bạn tạo Subscription thông qua Checkout Session, bạn cần cấu hình để metadata được truyền từ Session sang Subscription
    // Tham khảo: subscription_data.metadata trong lúc tạo Checkout Session
    if (
      metadata == null ||
      metadata.get("userId") == null ||
      metadata.get("userId").isBlank()
    ) {
      log.error(
        "User ID is missing or empty in subscription metadata for subscription id={}. Metadata: {}",
        subscriptionId,
        metadata
      );
      // Cân nhắc lấy userId từ User bằng customerId nếu metadata bị thiếu
      // Optional<User> userOpt = userRepository.findByStripeCustomerId(customerId);
      // if (userOpt.isEmpty()) {
      //    log.error("Cannot find User by customerId {} either.", customerId);
      //    return; // Không thể xác định user
      // }
      // User user = userOpt.get();
      // userId = user.getId(); // Lấy được userId
      // log.warn("Retrieved userId {} from customerId {} as subscription metadata was missing.", userId, customerId);

      // Tạm thời return nếu không có cách lấy userId dự phòng
      return;
    }
    String userIdStr = metadata.get("userId");

    Long userId;
    try {
      userId = Long.parseLong(userIdStr);
    } catch (NumberFormatException e) {
      log.error(
        "Invalid userId format '{}' in subscription metadata for subscription id={}",
        userIdStr,
        subscriptionId
      );
      return;
    }

    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty()) {
      log.error(
        "User not found for userId={} from subscription metadata (sub_id={})",
        userId,
        subscriptionId
      );
      return;
    }
    User user = userOptional.get();

    String stripePriceId = null;
    if (!subscription.getItems().getData().isEmpty()) {
      // Lấy Price ID từ item đầu tiên (giả định chỉ có 1 item trong subscription)
      stripePriceId =
        subscription.getItems().getData().get(0).getPrice().getId();
    } else {
      log.warn(
        "Subscription {} has no items, cannot determine stripePriceId.",
        subscriptionId
      );
      return; // Không có plan để liên kết
    }

    Optional<Plan> planOptional = planRepository.findByStripePriceId(
      stripePriceId
    );
    if (planOptional.isEmpty()) {
      log.error(
        "Plan not found for stripePriceId={} from subscription id={}",
        stripePriceId,
        subscriptionId
      );
      // Có thể bạn cần tạo Plan này trong DB hoặc kiểm tra lại Price ID
      return;
    }
    Plan plan = planOptional.get();

    String status = subscription.getStatus(); // active, trialing, past_due, canceled, incomplete, incomplete_expired, unpaid
    boolean isActiveOrValid =
      "active".equals(status) ||
      "trialing".equals(status) ||
      "past_due".equals(status);
    // "past_due" vẫn coi là active vì người dùng vẫn có thể truy cập trong grace period

    // Tìm UserSubscription hiện có bằng stripeSubscriptionId (ưu tiên)
    Optional<UserSubscription> existingSubscriptionOpt = userSubscriptionRepository.findByStripeSubscriptionId(
      subscriptionId
    );

    if (isActiveOrValid) {
      // Nếu subscription đang active/trialing/past_due -> Tạo mới hoặc Cập nhật
      UserSubscription userSubscription = existingSubscriptionOpt.orElseGet(() -> {
          log.info(
            "Creating new UserSubscription for userId={}, subscriptionId={}",
            userId,
            subscriptionId
          );
          UserSubscription newSub = new UserSubscription();
          newSub.setUser(user); // Chỉ set user khi tạo mới
          newSub.setPlan(plan); // Chỉ set plan khi tạo mới hoặc khi plan thay đổi (cần logic phức tạp hơn)
          newSub.setStripeSubscriptionId(subscriptionId);
          newSub.setStripeCustomerId(customerId);
          return newSub;
        }
      );

      // Luôn cập nhật các thông tin có thể thay đổi
      // Quan trọng: Nếu plan thay đổi (upgrade/downgrade), bạn cần cập nhật userSubscription.setPlan(plan);
      if (
        !userSubscription.getPlan().getStripePriceId().equals(stripePriceId)
      ) {
        log.info(
          "Plan changed for subscription {}. Old plan priceId: {}, New plan priceId: {}",
          subscriptionId,
          userSubscription.getPlan().getStripePriceId(),
          stripePriceId
        );
        userSubscription.setPlan(plan);
      }

      userSubscription.setStripeCurrentPeriodEnd(
        LocalDateTime.ofInstant(
          Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()),
          ZoneId.systemDefault()
        )
      );
      // Xử lý giá trị boolean có thể là null
      userSubscription.setStripeCancelAtPeriodEnd(
        Boolean.TRUE.equals(subscription.getCancelAtPeriodEnd())
      );
      // Cập nhật trạng thái (nếu bạn có trường status trong UserSubscription)
      // userSubscription.setStatus(status);

      userSubscriptionRepository.save(userSubscription);
      log.info(
        "Upserted UserSubscription for userId={}, subscriptionId={}, status={}",
        userId,
        subscriptionId,
        status
      );
    } else {
      // Nếu subscription không còn active (canceled, unpaid, incomplete, etc.)
      if (existingSubscriptionOpt.isPresent()) {
        // Có thể bạn muốn cập nhật trạng thái thay vì xóa ngay
        // UserSubscription subToDelete = existingSubscriptionOpt.get();
        // subToDelete.setStatus(status); // Cập nhật trạng thái thành 'canceled', 'unpaid' etc.
        // subToDelete.setStripeCancelAtPeriodEnd(true); // Đánh dấu hủy
        // userSubscriptionRepository.save(subToDelete);
        // log.info("Marked UserSubscription {} as inactive (status: {})", subscriptionId, status);

        // Hoặc xóa hẳn bản ghi nếu logic nghiệp vụ yêu cầu
        userSubscriptionRepository.delete(existingSubscriptionOpt.get());
        log.info(
          "Deleted UserSubscription for subscriptionId={} due to inactive status ({})",
          subscriptionId,
          status
        );
      } else {
        log.info(
          "Received inactive status ({}) for subscription {}, but no corresponding UserSubscription found.",
          status,
          subscriptionId
        );
      }
    }
  }

  // Xử lý khi subscription bị xóa hoàn toàn trên Stripe (không phải hủy vào cuối kỳ)
  @Transactional
  void handleSubscriptionDeleted(Subscription subscription) {
    String subscriptionId = subscription.getId();
    Optional<UserSubscription> existingSubscriptionOpt = userSubscriptionRepository.findByStripeSubscriptionId(
      subscriptionId
    );
    if (existingSubscriptionOpt.isPresent()) {
      userSubscriptionRepository.delete(existingSubscriptionOpt.get());
      log.info(
        "Deleted UserSubscription for subscriptionId={} due to customer.subscription.deleted event",
        subscriptionId
      );
    } else {
      log.warn(
        "Received customer.subscription.deleted event for subscriptionId={}, but no corresponding UserSubscription found.",
        subscriptionId
      );
    }
  }
}
