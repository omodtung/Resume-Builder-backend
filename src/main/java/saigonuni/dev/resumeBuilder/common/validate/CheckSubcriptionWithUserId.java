package saigonuni.dev.resumeBuilder.common.validate;

import java.util.List;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.common.enums.Plan;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;
import saigonuni.dev.resumeBuilder.message.UserSubcription;
import saigonuni.dev.resumeBuilder.service.UserSubcriptionService;

@Service
public class CheckSubcriptionWithUserId {

  private UserSubcriptionService userSubcriptionService;

  public CheckSubcriptionWithUserId(
    UserSubcriptionService userSubcriptionService
  ) {
    this.userSubcriptionService = userSubcriptionService;
  }

  public Boolean checkServiceUsage(User userId, Long planId) {
    List<UserSupcriptionDTO> userSubGet = userSubcriptionService.FetchDataUserSubWithPlanWithUser(
      userId.getId()
    );

    for (UserSupcriptionDTO userSub : userSubGet) {
      if (
        userSub.getUser().getId() == userId.getId() &&
        userSub.getPlan().getId() == planId
      ) {
        if (userSub.getStripeCurrentPeriodEnd() != null) {
          if (
            userSub
              .getStripeCurrentPeriodEnd()
              .isAfter(java.time.LocalDateTime.now())
          ) {
            throw new BadRequestException(
              UserSubcription.USER_SUBSCRIPTION_EXPIRED,
              UserSubcription.USER_SUBSCRIPTION_EXPIRED_MESSAGE
            );
          }
        }

        return true;
      }
    }
    return null;
  }

  public void permissionForEachPlan(Long userId) {
    UserSupcriptionDTO userSubGet = userSubcriptionService.findUserActivateSubscription(
      userId, // Corrected: Pass userId directly
      true
    );
    // Corrected: Check only for null, as userSubGet is an object, not a collection
    if (userSubGet == null) {
      throw new BadRequestException(
        UserSubcription.USER_SUBSCRIPTION_NOT_FOUND,
        UserSubcription.USER_SUBSCRIPTION_NOT_FOUND_MESSAGE
      );
    }

    // Corrected: Use .equals() for String comparison and compare with enum's name()
    if (Plan.BASIC.name().equals(userSubGet.getPlan().getPlansName())) {
      System.out.println("User has a basic plan");
    }
    if (Plan.PREMIUM.name().equals(userSubGet.getPlan().getPlansName())) {
      System.out.println("User has a premium plan");
    }
    if (Plan.FREE.name().equals(userSubGet.getPlan().getPlansName())) {
      System.out.println("User has a free plan");
    }
  }
}
