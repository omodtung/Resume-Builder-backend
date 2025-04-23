package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Plan; // Added import
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.dto.Plan.PlanSubscription;
import saigonuni.dev.resumeBuilder.dto.User.UserSubDTO;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;
import saigonuni.dev.resumeBuilder.repository.UserSubscriptionRepository;

@Service
public class UserSubscriptionServiceImplement
  implements UserSubcriptionService {

  private final UserSubscriptionRepository userSubscriptionRepository;

  public UserSubscriptionServiceImplement(
    UserSubscriptionRepository userSubscriptionRepository
  ) {
    this.userSubscriptionRepository = userSubscriptionRepository;
  }

  @Override
  public List<UserSupcriptionDTO> FetchDataUserSubWithPlanWithUser(
    Long userId
  ) {
    try {
      // Fetch entities from the repository
      List<UserSubscription> subscriptions = userSubscriptionRepository.FetchDataUserSubWithPlanWithUser(
        userId
      );

      // Check if the result is null or empty
      if (subscriptions == null || subscriptions.isEmpty()) {
        System.err.println("No subscriptions found for user ID: " + userId);
        return List.of(); // Return an empty list
      }

      // Map the entities to DTOs
      return subscriptions
        .stream()
        .map(subscription -> {
          UserSupcriptionDTO.UserSupcriptionDTOBuilder builder = UserSupcriptionDTO.builder();

          // Map User fields
          if (subscription.getUser() != null) {
            builder.user(
              new UserSubDTO(
                subscription.getUser().getId(),
                subscription.getUser().getUsername(),
                subscription.getUser().getEmail()
              )
            );
          }

          // Map Plan fields
          if (subscription.getPlan() != null) {
            PlanSubscription planSubDTO = new PlanSubscription();
            planSubDTO.setId(subscription.getPlan().getId());
            planSubDTO.setPlansName(subscription.getPlan().getPlansName());
            planSubDTO.setDescription(subscription.getPlan().getDescription());
            planSubDTO.setPrice(subscription.getPlan().getPrice());
            builder.plan(planSubDTO);
          }

          // Map other fields
          builder.id(subscription.getId());
          builder.stripeCustomerId(subscription.getStripeCustomerId());
          builder.stripeSubscriptionId(subscription.getStripeSubscriptionId());
          builder.stripeCurrentPeriodEnd(
            subscription.getStripeCurrentPeriodEnd()
          );
          builder.stripeCancelAtPeriodEnd(
            subscription.getStripeCancelAtPeriodEnd()
          );

          return builder.build();
        })
        .collect(Collectors.toList());
    } catch (Exception e) {
      // Log the exception properly in a real application
      System.err.println(
        "Error fetching subscriptions with plan and user: " + e.getMessage()
      );
      e.printStackTrace(); // Added stack trace for debugging
      return List.of(); // Return empty list on error for now
    }
  }

  @Override
  public UserSupcriptionDTO findUserActivateSubscription(
    Long userId,
    Boolean isActive
  ) {
    try {
      // Fetch entity from the repository
      UserSubscription subscription =
        userSubscriptionRepository.findUserActivateSubscription(
          userId,
          isActive
        );

      // Check if the result is null
      if (subscription == null) {
        System.err.println("No active subscription found for user ID: " + userId);
        return null; // Return null if no subscription is found
      }

      // Map the entity to DTO
      UserSupcriptionDTO.UserSupcriptionDTOBuilder builder = UserSupcriptionDTO.builder();

      // Map User fields
      if (subscription.getUser() != null) {
        builder.user(
          new UserSubDTO(
            subscription.getUser().getId(),
            subscription.getUser().getUsername(),
            subscription.getUser().getEmail()
          )
        );
      }

      // Map Plan fields
      if (subscription.getPlan() != null) {
        PlanSubscription planSubDTO = new PlanSubscription();
        planSubDTO.setId(subscription.getPlan().getId());
        planSubDTO.setPlansName(subscription.getPlan().getPlansName());
        planSubDTO.setDescription(subscription.getPlan().getDescription());
        planSubDTO.setPrice(subscription.getPlan().getPrice());
        builder.plan(planSubDTO);
      }

      // Map other fields
      builder.id(subscription.getId());
      builder.stripeCustomerId(subscription.getStripeCustomerId());
      builder.stripeSubscriptionId(subscription.getStripeSubscriptionId());
      builder.stripeCurrentPeriodEnd(subscription.getStripeCurrentPeriodEnd());
      builder.stripeCancelAtPeriodEnd(subscription.getStripeCancelAtPeriodEnd());

      return builder.build();
    } catch (Exception e) {
      // Log the exception properly in a real application
      System.err.println(
        "Error fetching active subscription for user: " + e.getMessage()
      );
      e.printStackTrace(); // Added stack trace for debugging
      return null; // Return null on error
    }
  }

  @Override
  public List<UserSubscription> FetchDataUserSub() {
    try {
      // Fetch data from the repository
      List<UserSubscription> subscriptions = userSubscriptionRepository.FetchDataUserSub(); // Fixed method name
      return subscriptions;
    } catch (Exception e) {
      System.err.println(
        "Error fetching user subscriptions: " + e.getMessage()
      );
      e.printStackTrace(); // Added stack trace for debugging
      return List.of(); // Return empty list on error
    }
  }

  private UserSupcriptionDTO mapToDTO(UserSubscription entity) {
    // Use the builder pattern generated by Lombok
    UserSupcriptionDTO.UserSupcriptionDTOBuilder builder = UserSupcriptionDTO.builder();

    // Map User fields if User is not null
    if (entity.getUser() != null) {
      Long userId = entity.getUser().getId(); // Assumes getId() exists
      String username = entity.getUser().getUsername(); // Assumes getUsername() exists
      String email = entity.getUser().getEmail(); // Assumes getEmail() exists

      // Use the correct constructor for UserSubDTO
      if (userId != null && username != null && email != null) {
        builder.user(new UserSubDTO(userId, username, email));
      } else {
        System.err.println(
          "User fields (id, username, or email) are null for UserSubscription ID: " +
          entity.getId()
        );
      }
    } else {
      System.err.println(
        "User is null for UserSubscription ID: " + entity.getId()
      );
    }

    // Map Plan fields if Plan is not null
    if (entity.getPlan() != null) {
      Plan planEntity = entity.getPlan();
      PlanSubscription planSubDTO = new PlanSubscription();
      planSubDTO.setId(planEntity.getId()); // Assumes getId() exists
      planSubDTO.setPlansName(planEntity.getPlansName()); // Use correct getter
      planSubDTO.setDescription(planEntity.getDescription()); // Assumes getDescription() exists
      planSubDTO.setPrice(planEntity.getPrice()); // Assumes getPrice() exists

      builder.plan(planSubDTO);
    } else {
      System.err.println(
        "Plan is null for UserSubscription ID: " + entity.getId()
      );
    }

    // Map other fields of UserSubscription
    builder.id(entity.getId());
    builder.stripeCustomerId(entity.getStripeCustomerId());
    builder.stripeSubscriptionId(entity.getStripeSubscriptionId());
    builder.stripeCurrentPeriodEnd(entity.getStripeCurrentPeriodEnd());
    builder.stripeCancelAtPeriodEnd(entity.getStripeCancelAtPeriodEnd());

    return builder.build();
  }
}
