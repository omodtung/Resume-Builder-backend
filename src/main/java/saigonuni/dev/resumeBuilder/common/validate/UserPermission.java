package saigonuni.dev.resumeBuilder.common.validate;

import java.util.List;

import org.springframework.stereotype.Service;

import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;
import saigonuni.dev.resumeBuilder.message.UserSubcription;
import saigonuni.dev.resumeBuilder.repository.UserRepository;
import saigonuni.dev.resumeBuilder.service.UserSubcriptionService;
import saigonuni.dev.resumeBuilder.service.UserValueImplement;
@Service
public class UserPermission {
    

  public UserPermission(
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

}
