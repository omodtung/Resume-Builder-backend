package saigonuni.dev.resumeBuilder.common.validate;

import java.util.List;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.common.enums.Plan;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;
import saigonuni.dev.resumeBuilder.message.UserSubcription;
import saigonuni.dev.resumeBuilder.repository.UserRepository;
import saigonuni.dev.resumeBuilder.service.UserSubcriptionService;
import saigonuni.dev.resumeBuilder.service.UserValueImplement;

@Service
public class CheckSubcriptionWithUserId {

  private UserSubcriptionService userSubcriptionService;
  private UserValueImplement userValueImplement;
  private UserRepository UserRepository;

  public CheckSubcriptionWithUserId(
    UserSubcriptionService userSubcriptionService,
    UserValueImplement userValueImplement,
    UserRepository userRepository
  ) {
    this.userSubcriptionService = userSubcriptionService;
    this.userValueImplement = userValueImplement;
    this.UserRepository = userRepository;
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
    // todo fix this error not thrown exacly name of error
    if (userSubGet == null) {
      throw new BadRequestException(
        UserSubcription.USER_SUBSCRIPTION_NOT_FOUND,
        UserSubcription.USER_SUBSCRIPTION_NOT_FOUND_MESSAGE
      );
    }

    // Corrected: Use .equals() for String comparison and compare with enum's name()
    if (Plan.BASIC.name().equals(userSubGet.getPlan().getPlansName())) {
      int countUser = userValueImplement.CountCvCreatedByUserId(userId);
      if (countUser > 4) {
        throw new BadRequestException(
          UserSubcription.LIMIT_BASIC_REACT,
          UserSubcription.LIMIT_BASIC_REACT_MESSAGE
        );
      }
    }

    if (Plan.PREMIUM.name().equals(userSubGet.getPlan().getPlansName())) {}

    if (Plan.FREE.name().equals(userSubGet.getPlan().getPlansName())) {
      int countUser = userValueImplement.CountCvCreatedByUserId(userId);
      if (countUser > 3) {
        throw new BadRequestException(
          UserSubcription.LIMIT_FREE_REACT_KEY,
          UserSubcription.LIMIT_FREE_REACT_MESSAGE
        );
      }
    }
  }

  public  void checkPlanUsingAifeature(String user_name) {
    User user = UserRepository.findByUsername(user_name);
    if (user == null) {
      throw new BadRequestException(
        UserSubcription.USER_SUBSCRIPTION_NOT_FOUND,
        UserSubcription.USER_SUBSCRIPTION_NOT_FOUND_MESSAGE
      );
    }
    UserSupcriptionDTO userSubGet = userSubcriptionService.findUserActivateSubscription(
      user.getId(), // Corrected: Pass userId directly
      true
    );

    if (userSubGet == null) {
      throw new BadRequestException(
        UserSubcription.USER_SUBSCRIPTION_NOT_FOUND,
        UserSubcription.USER_SUBSCRIPTION_NOT_FOUND_MESSAGE
      );
    }

    if (Plan.BASIC.name().equals(userSubGet.getPlan().getPlansName())) {
      throw new BadRequestException(
        UserSubcription.BASIC_AND_FREE_NO_PERMISSION_USE_KEY,
        UserSubcription.BASIC_AND_FREE_NO_PERMISSION_USE_MESSAGE
      );
    }

    if (Plan.PREMIUM.name().equals(userSubGet.getPlan().getPlansName())) {}

    if (Plan.FREE.name().equals(userSubGet.getPlan().getPlansName())) {
      throw new BadRequestException(
        UserSubcription.BASIC_AND_FREE_NO_PERMISSION_USE_KEY,
        UserSubcription.BASIC_AND_FREE_NO_PERMISSION_USE_MESSAGE
      );
    }
    
  }
}
