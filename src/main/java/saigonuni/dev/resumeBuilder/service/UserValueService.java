package saigonuni.dev.resumeBuilder.service;

import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue;
import saigonuni.dev.resumeBuilder.dto.UserValue.CreateUserValueRequest;

public interface UserValueService {
  UserValue save(CreateUserValueRequest userValue);
  UserValue getUserValueById(String id);
  UserValue getUserValueByUserId(String userId);
  UserValue createUserValueForUser(User user);
}
