package saigonuni.dev.resumeBuilder.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
// import saigonuni.dev.resumeBuilder.common.enums.UserSubcription;
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

  // @Value("${stripe.api.key}")
  private String stripeApiKey =
    "sk_test_51QuBaRK6dhIc7YOm3TJxxTw409Wxuzkk6Mis7LVPEuitjglkIn3zChdiE6jweMVW7Mn4Na8WCTLJPymFvbL7mh4v00azltuaWj";

  // @Value("${stripe.webhook.secret}")
  // !!! Quan trọng: Đảm bảo secret này khớp với endpoint đang gửi phiên bản .acacia
  private String endpointSecret = "whsec_fVqd42xr6WDkutkorecqZMsHwJtBcZXU";

  private final UserSubscriptionRepository userSubscriptionRepository;
  private final UserRepository userRepository;
  private final PlanRepository planRepository;
  private final ObjectMapper objectMapper = new ObjectMapper(); // Tạo instance ObjectMapper

  @PostConstruct
  public void initStripe() {
    if (stripeApiKey == null || stripeApiKey.isBlank()) {
      log.error("Stripe API key is not configured!");
    } else {
      Stripe.apiKey = stripeApiKey;
      // Có thể set API version ở đây, nhưng nó không giúp deserialize phiên bản .acacia
      // Stripe.apiVersion = "2024-04-10"; // Ví dụ
      log.info("Stripe API Key initialized."); // Stripe API Version is: {}", Stripe.apiVersion);
    }
    if (
      endpointSecret == null ||
      endpointSecret.isBlank() ||
      !endpointSecret.startsWith("whsec_")
    ) {
      log.error("Stripe webhook secret is not configured correctly!");
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

    if (endpointSecret == null || endpointSecret.isBlank()) {
      log.error("Stripe webhook secret is missing during request processing!");
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Webhook processor configuration error.");
    }

    try {
      // Vẫn xác thực chữ ký và tạo đối tượng Event cơ bản
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
    );

    // --- Bỏ qua deserialization tự động, xử lý JSON thủ công ---
    String rawJsonData = null;
    JsonNode objectNode = null; // Node chứa object chính (Session, Subscription,...)

    if (event.getData() != null && event.getData().getObject() != null) {
      rawJsonData = event.getData().getObject().toJson(); // Lấy JSON của object bên trong data
      log.debug(
        "Raw JSON data object for event {}: {}",
        event.getId(),
        rawJsonData
      );
      try {
        // Phân tích JSON thủ công bằng Jackson
        objectNode = objectMapper.readTree(rawJsonData);
      } catch (IOException e) {
        log.error(
          "Failed to manually parse JSON data for event id={}, type={}. Raw data: {}",
          event.getId(),
          event.getType(),
          rawJsonData,
          e
        );
        return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("JSON parsing error");
      }
    } else {
      log.error(
        "Event data or data object is null for event id={}, type={}",
        event.getId(),
        event.getType()
      );
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Invalid event data");
    }

    // --- Event Handling với JSON đã phân tích ---
    try {
      // Kiểm tra null trước khi sử dụng objectNode
      if (objectNode == null) {
        log.error(
          "Parsed JSON object node is null for event id={}",
          event.getId()
        );
        return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("JSON parsing resulted in null object");
      }

      switch (event.getType()) {
        case "checkout.session.completed":
          // Kiểm tra xem objectNode có phải là checkout.session không
          if ("checkout.session".equals(objectNode.path("object").asText())) {
            handleSessionCompletedManually(objectNode); // Gọi hàm xử lý thủ công
          } else {
            log.warn(
              "Expected 'checkout.session' object type in JSON for event {}, but got '{}'",
              event.getId(),
              objectNode.path("object").asText()
            );
          }
          break;
        case "customer.subscription.created":
        case "customer.subscription.updated":
          if ("subscription".equals(objectNode.path("object").asText())) {
            handleSubscriptionCreatedOrUpdatedManually(objectNode); // Gọi hàm xử lý thủ công
          } else {
            log.warn(
              "Expected 'subscription' object type in JSON for event {}, but got '{}'",
              event.getId(),
              objectNode.path("object").asText()
            );
          }
          break;
        case "customer.subscription.deleted":
          if ("subscription".equals(objectNode.path("object").asText())) {
            handleSubscriptionDeletedManually(objectNode); // Gọi hàm xử lý thủ công
          } else {
            log.warn(
              "Expected 'subscription' object type in JSON for event {}, but got '{}'",
              event.getId(),
              objectNode.path("object").asText()
            );
          }
          break;
        // Thêm các case khác nếu cần và xử lý JSON thủ công tương tự

        default:
          log.info(
            "Unhandled event type (manual handling not implemented): {}",
            event.getType()
          );
          break;
      }
      // Trả về 200 OK cho Stripe
      return ResponseEntity.ok("Event received");
    } catch (StripeException e) { // Vẫn có thể xảy ra nếu gọi API Stripe trong hàm xử lý
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
        "Application error processing event id={} manually: {}",
        event.getId(),
        e.getMessage(),
        e
      );
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Internal server error during processing");
    }
  }

  // --- Các hàm xử lý được sửa đổi để nhận JsonNode hoặc dữ liệu trích xuất ---

  @Transactional
  void handleSessionCompletedManually(JsonNode sessionNode) {
    String sessionId = sessionNode.path("id").asText(null);
    String customerId = sessionNode.path("customer").asText(null);
    String subscriptionId = sessionNode.path("subscription").asText(null); // Lấy subscription ID nếu có

    // Trích xuất userId từ metadata một cách an toàn
    String userIdStr = null;
    if (
      sessionNode.has("metadata") && sessionNode.path("metadata").isObject()
    ) {
      userIdStr = sessionNode.path("metadata").path("userId").asText(null);
    }

    if (sessionId == null) {
      log.error(
        "Session ID is missing in manually parsed checkout.session object. Node: {}",
        sessionNode.toString()
      );
      return;
    }

    // Kiểm tra userId và customerId
    if (userIdStr == null || userIdStr.isBlank()) {
      log.error(
        "User ID is missing or empty in session metadata for session id={}. Metadata Node: {}",
        sessionId,
        sessionNode.path("metadata").toString()
      );
      // QUAN TRỌNG: Đảm bảo bạn luôn gửi userId trong metadata khi tạo Checkout Session
      return;
    }
    if (customerId == null) {
      log.error(
        "Customer ID is missing in session object for session id={}",
        sessionId
      );
      return;
    }

    log.info(
      "Processing checkout.session.completed (manually parsed) for session_id={}, customerId={}, userId={}",
      sessionId,
      customerId,
      userIdStr
    );

    // --- Logic cập nhật User (nếu cần) ---
    try {
      Long userId = Long.parseLong(userIdStr);
      Optional<User> userOptional = userRepository.findById(userId);

      if (userOptional.isPresent()) {
        User user = userOptional.get();
        // Cập nhật stripeCustomerId nếu cần (logic tương tự như trước)
        // ...

        // update Status IsActive of UserSubcription before using a new Plan
        // userSubscriptionRepository.updateActiveByUserId(user.getId(), false);
      } else {
        log.warn(
          "User not found for id={} during manual session completion handling (session id={}).",
          userId,
          sessionId
        );
      }
    } catch (NumberFormatException e) {
      log.error(
        "Invalid userId format '{}' in session metadata for session id={}",
        userIdStr,
        sessionId
      );
    } catch (Exception e) {
      log.error(
        "Error processing manual session completed logic for session id={}: {}",
        sessionId,
        e.getMessage(),
        e
      );
    }

    log.info(
      "Manual checkout session {} completed. Subscription ID (if any): {}. Waiting for subsequent events.",
      sessionId,
      subscriptionId
    );
    // Lưu ý: Thông tin chi tiết khác của Session (như line items, amount, status)
    // cũng có thể được trích xuất từ sessionNode nếu cần.
    // Ví dụ: String status = sessionNode.path("status").asText();
  }

  @Transactional
  void handleSubscriptionCreatedOrUpdatedManually(JsonNode subscriptionNode)
    throws StripeException {
    String subscriptionId = subscriptionNode.path("id").asText(null);
    String customerId = subscriptionNode.path("customer").asText(null);
    String status = subscriptionNode.path("status").asText(null); // active, trialing, etc.
    long currentPeriodEndEpoch = subscriptionNode
      .path("current_period_end")
      .asLong(0);
    boolean cancelAtPeriodEnd = subscriptionNode
      .path("cancel_at_period_end")
      .asBoolean(false);

    if (subscriptionId == null) {
      log.error(
        "Subscription ID is missing in manually parsed subscription object. Node: {}",
        subscriptionNode.toString()
      );
      return;
    }

    // Trích xuất userId từ metadata của Subscription
    String userIdStr = null;
    if (
      subscriptionNode.has("metadata") &&
      subscriptionNode.path("metadata").isObject()
    ) {
      userIdStr = subscriptionNode.path("metadata").path("userId").asText(null);
    }

    if (userIdStr == null || userIdStr.isBlank()) {
      log.error(
        "User ID is missing or empty in subscription metadata for subscription id={}. Metadata Node: {}",
        subscriptionId,
        subscriptionNode.path("metadata").toString()
      );
      // QUAN TRỌNG: Đảm bảo bạn gửi userId trong subscription_data.metadata khi tạo Checkout Session
      return;
    }

    if (customerId == null || status == null || currentPeriodEndEpoch == 0) {
      log.error(
        "Required fields (customerId, status, current_period_end) missing for subscription id={}",
        subscriptionId
      );
      return;
    }

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

    // update before plan become false
    // userSubscriptionRepository.updateActiveByUserId(user.getId(), false);

    // Trích xuất Price ID từ items
    String stripePriceId = null;
    if (
      subscriptionNode.has("items") &&
      subscriptionNode.path("items").has("data") &&
      subscriptionNode.path("items").path("data").isArray()
    ) {
      JsonNode itemsData = subscriptionNode.path("items").path("data");
      if (itemsData.size() > 0) {
        JsonNode firstItem = itemsData.get(0);
        if (firstItem.has("price") && firstItem.path("price").has("id")) {
          stripePriceId = firstItem.path("price").path("id").asText(null);
        }
      }
    }

    if (stripePriceId == null) {
      log.warn(
        "Could not determine stripePriceId from subscription items for sub_id={}. Items node: {}",
        subscriptionId,
        subscriptionNode.path("items").toString()
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
      return;
    }
    Plan plan = planOptional.get();

    userSubscriptionRepository.updateActiveByUserId(user.getId(), false, plan.getId());

    boolean isActiveOrValid =
      "active".equals(status) ||
      "trialing".equals(status) ||
      "past_due".equals(status);

    Optional<UserSubscription> existingSubscriptionOpt = userSubscriptionRepository.findByStripeSubscriptionId(
      subscriptionId
    );

    if (isActiveOrValid) {
      UserSubscription userSubscription = existingSubscriptionOpt.orElseGet(() -> {
          log.info(
            "Creating new UserSubscription (manually) for userId={}, subscriptionId={}",
            userId,
            subscriptionId
          );
          UserSubscription newSub = new UserSubscription();
          newSub.setUser(user);
          newSub.setPlan(plan);
          newSub.setStripeSubscriptionId(subscriptionId);
          newSub.setStripeCustomerId(customerId);
          return newSub;
        }
      );

      // Cập nhật Plan nếu thay đổi
      if (
        !userSubscription.getPlan().getStripePriceId().equals(stripePriceId)
      ) {
        log.info(
          "Plan changed (manually) for subscription {}. Old: {}, New: {}",
          subscriptionId,
          userSubscription.getPlan().getStripePriceId(),
          stripePriceId
        );
        userSubscription.setPlan(plan);
      }

      userSubscription.setStripeCurrentPeriodEnd(
        LocalDateTime.ofInstant(
          Instant.ofEpochSecond(currentPeriodEndEpoch),
          ZoneId.systemDefault()
        )
      );
      userSubscription.setStripeCancelAtPeriodEnd(cancelAtPeriodEnd);
      // userSubscription.setStatus(status); // Nếu có trường status

      userSubscriptionRepository.save(userSubscription);
      log.info(
        "Upserted UserSubscription (manually) for userId={}, subscriptionId={}, status={}",
        userId,
        subscriptionId,
        status
      );
    } else { // Subscription không còn active
      if (existingSubscriptionOpt.isPresent()) {
        userSubscriptionRepository.delete(existingSubscriptionOpt.get());
        log.info(
          "Deleted UserSubscription (manually) for subscriptionId={} due to inactive status ({})",
          subscriptionId,
          status
        );
      } else {
        log.info(
          "Received inactive status ({}) (manually) for subscription {}, but no corresponding UserSubscription found.",
          status,
          subscriptionId
        );
      }
    }
  }

  @Transactional
  void handleSubscriptionDeletedManually(JsonNode subscriptionNode) {
    String subscriptionId = subscriptionNode.path("id").asText(null);
    if (subscriptionId == null) {
      log.error(
        "Subscription ID is missing in manually parsed subscription object for deletion. Node: {}",
        subscriptionNode.toString()
      );
      return;
    }

    log.info(
      "Handling customer.subscription.deleted (manually parsed) for subscription_id={}",
      subscriptionId
    );
    long deletedCount = userSubscriptionRepository.deleteByStripeSubscriptionId(
      subscriptionId
    );
    if (deletedCount > 0) {
      log.info(
        "Deleted {} UserSubscription(s) (manually) for subscriptionId={} due to deletion event",
        deletedCount,
        subscriptionId
      );
    } else {
      log.warn(
        "Received deletion event (manually) for subscriptionId={}, but no corresponding UserSubscription found.",
        subscriptionId
      );
    }
  }
}
