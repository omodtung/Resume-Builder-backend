package saigonuni.dev.resumeBuilder.service;

import saigonuni.dev.resumeBuilder.domain.UserValue;
import saigonuni.dev.resumeBuilder.dto.UserValue.CreateUserValueRequest;

public interface UserValueService {
  UserValue save(CreateUserValueRequest userValue);
}
