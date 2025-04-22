package saigonuni.dev.resumeBuilder.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.dto.Plan.PlanSubscription;
import saigonuni.dev.resumeBuilder.dto.User.UserSubDTO;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;
import saigonuni.dev.resumeBuilder.repository.UserSubscriptionRepository;

@Service
public class UserSubscriptionServiceImplement
  implements UserSubcriptionService {

  private UserSubscriptionRepository userSubscriptionRepository;

  public UserSubscriptionServiceImplement(
    UserSubscriptionRepository userSubscriptionRepository
  ) {
    this.userSubscriptionRepository = userSubscriptionRepository;
  }

  // @Override
  // public List<UserSupcriptionDTO> fetchUserWithSpecialPlan(Long userId) {
  //   // Fetch data from the repository
  //   List<UserSubscription> subscriptions = userSubscriptionRepository.FetchDataUserSubWithPlan(
  //     userId
  //   );

  //   // Check if the result is null or empty
  //   if (subscriptions == null || subscriptions.isEmpty()) {
  //     System.err.println("No subscriptions found for user ID: " + userId);
  //     return List.of(); // Return an empty list
  //   }

  //   // Map the entities to DTOs
  //   return subscriptions
  //     .stream()
  //     .map(this::mapToDTO)
  //     .collect(Collectors.toList());
  // }

  // private UserSupcriptionDTO mapToDTO(UserSubscription entity) {
  //   UserSupcriptionDTO dto = new UserSupcriptionDTO();

  //   // Map User fields if User is not null
  //   if (entity.getUser() != null) {
  //     Long userId = entity.getUser().getId();
  //     String username = entity.getUser().getUsername();

  //     if (userId != null && username != null) {
  //       dto.setUser(new UserSubDTO(userId, username));
  //     } else {
  //       // Log or handle cases where user fields are null
  //       System.err.println(
  //         "User fields are null for UserSubscription ID: " + entity.getId()
  //       );
  //     }
  //   } else {
  //     System.err.println(
  //       "User is null for UserSubscription ID: " + entity.getId()
  //     );
  //   }

  //   // Map Plan fields if Plan is not null
  //   if (entity.getPlan() != null) {
  //     String stripePriceId = entity.getPlan().getStripePriceId();
  //     String planName = entity.getPlan().getName();

  //     if (stripePriceId != null && planName != null) {
  //       dto.setPlan(new PlanSubscription(stripePriceId, planName));
  //     } else {
  //       // Log or handle cases where plan fields are null
  //       System.err.println(
  //         "Plan fields are null for UserSubscription ID: " + entity.getId()
  //       );
  //     }
  //   } else {
  //     System.err.println(
  //       "Plan is null for UserSubscription ID: " + entity.getId()
  //     );
  //   }

  //   // Map other fields of UserSubscription
  //   dto.setId(entity.getId());
  //   dto.setStripeCustomerId(entity.getStripeCustomerId());
  //   dto.setStripeSubscriptionId(entity.getStripeSubscriptionId());
  //   dto.setStripeCurrentPeriodEnd(entity.getStripeCurrentPeriodEnd());
  //   dto.setStripeCancelAtPeriodEnd(entity.getStripeCancelAtPeriodEnd());

  //   return dto;
  // }

  @Override
  public List<UserSupcriptionDTO> FetchDataUserSubWithPlanWithUser(
    Long userId
  ) {
    List<UserSupcriptionDTO> subscriptions = userSubscriptionRepository.FetchDataUserSubWithPlanWithUser( Long userId );
    return subscriptions;
  }

  @Override
  public List<UserSubscription> FetchDataUserSub() {
    // Fetch data from the repository
    List<UserSubscription> subscriptions = userSubscriptionRepository.FetchDataUserSub();

    return subscriptions;
  }
}
