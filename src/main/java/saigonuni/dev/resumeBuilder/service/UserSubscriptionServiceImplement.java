package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saigonuni.dev.resumeBuilder.domain.Plan;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.CreateUserSubscriptionRequest;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UpdateUserSubscriptionRequest;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;
import saigonuni.dev.resumeBuilder.message.CommonMessage;
import saigonuni.dev.resumeBuilder.repository.PlanRepository;
import saigonuni.dev.resumeBuilder.repository.UserSubscriptionRepository;

@Service
@Slf4j
public class UserSubscriptionServiceImplement
  implements UserSubscriptionService {

  private final UserSubscriptionRepository userSubscriptionRepository;
  private final PlanRepository planRepository;

  @Autowired
  public UserSubscriptionServiceImplement(
    UserSubscriptionRepository userSubscriptionRepository,
    PlanRepository planRepository
  ) {
    this.userSubscriptionRepository = userSubscriptionRepository;
    this.planRepository = planRepository;
  }

  @Override
  @Transactional
  public UserSubscription addUserSubscription(
    CreateUserSubscriptionRequest request,
    User user
  ) {
    try {
      UserSubscription userSubscription = new UserSubscription();
      userSubscription.setUser(user);
      Optional<Plan> plan = planRepository.findById(request.getPlanId());
      if (plan.isPresent()) {
        userSubscription.setPlan(plan.get());
      } else {
        throw new BadRequestException("Plan not found", "Plan not found");
      }
      log.info("Adding new UserSubscription for user: {}", user.getId());
      return userSubscriptionRepository.save(userSubscription);
    } catch (Exception e) {
      log.error("Error adding UserSubscription: {}", e.getMessage());
      throw new RuntimeException("Error adding UserSubscription", e);
    }
  }

  @Override
  public UserSubscription getUserSubscriptionById(Long id) {
    log.info("Fetching UserSubscription by ID: {}", id);
    return userSubscriptionRepository
      .findById(id)
      .orElseThrow(() -> {
        log.warn("UserSubscription not found for ID: {}", id);
        return new BadRequestException(
          "USER_SUBSCRIPTION_NOT_FOUND",
          "User Subscription not found"
        );
      });
  }

  @Override
  public List<UserSubscription> listUserSubscriptions() {
    log.info("Fetching all UserSubscriptions");
    return userSubscriptionRepository.findAll();
  }

  @Override
  @Transactional
  public UserSubscription updateUserSubscription(
    Long id,
    UpdateUserSubscriptionRequest request,
    User user
  ) {
    try {
      UserSubscription userSubscription = getUserSubscriptionById(id);
      Optional<Plan> plan = planRepository.findById(request.getPlanId());
      if (plan.isPresent()) {
        userSubscription.setPlan(plan.get());
      } else {
        throw new BadRequestException("Plan not found", "Plan not found");
      }
      log.info("Updating UserSubscription with ID: {}", id);
      return userSubscriptionRepository.save(userSubscription);
    } catch (Exception e) {
      log.error(
        "Error updating UserSubscription with ID {}: {}",
        id,
        e.getMessage()
      );
      throw new RuntimeException("Error updating UserSubscription", e);
    }
  }

  @Override
  public void deleteUserSubscription(Long id) {
    // Basic placeholder implementation - Needs actual logic
    try {
      UserSubscription userSubscription = getUserSubscriptionById(id); // Reuse getById logic
      log.info("Deleting UserSubscription with ID: {}", id);
      userSubscriptionRepository.delete(userSubscription);
    } catch (Exception e) {
      log.error(
        "Error deleting UserSubscription with ID {}: {}",
        id,
        e.getMessage()
      );
      // Consider more specific exception handling
      throw new RuntimeException("Error deleting UserSubscription", e);
    }
  }
}
