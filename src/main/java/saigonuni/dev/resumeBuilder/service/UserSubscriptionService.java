package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
// Placeholder imports - These DTOs need to be created
import saigonuni.dev.resumeBuilder.dto.UserSubscription.CreateUserSubscriptionRequest;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UpdateUserSubscriptionRequest;

public interface UserSubscriptionService {
  UserSubscription addUserSubscription(CreateUserSubscriptionRequest request, User user);
  UserSubscription getUserSubscriptionById(Long id);
  List<UserSubscription> listUserSubscriptions();
  UserSubscription updateUserSubscription(Long id, UpdateUserSubscriptionRequest request, User user);
  void deleteUserSubscription(Long id);
}
