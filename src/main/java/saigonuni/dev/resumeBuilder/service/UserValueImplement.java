package saigonuni.dev.resumeBuilder.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue;
import saigonuni.dev.resumeBuilder.dto.UserValue.CreateUserValueRequest;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;
import saigonuni.dev.resumeBuilder.exception.DuplicateKeyException;
import saigonuni.dev.resumeBuilder.exception.UserValueNotFoundException;
import saigonuni.dev.resumeBuilder.message.CommonMessage;
import saigonuni.dev.resumeBuilder.repository.UserValueRepository;

@Service
@Slf4j
public class UserValueImplement implements UserValueService {

  @Autowired
  private UserValueRepository useValueRepository;

  @Override
  public UserValue save(CreateUserValueRequest userValue) {
    try {
      UserValue userValues = UserValue
        .builder()
        .createdAt(LocalDateTime.now())
        // .resume(userValue.getResume())
        .user(userValue.getUser())
        .build();

      return useValueRepository.save(userValues);
    } catch (DataIntegrityViolationException e) {
      throw new DuplicateKeyException(CommonMessage.DUPLICATE_KEY.getMessage());
    }
  }

  @Override
  public UserValue getUserValueById(String id) {
    try {
      return useValueRepository
        .findById(Long.valueOf(id))
        .orElseThrow(() -> new UserValueNotFoundException());
    } catch (NumberFormatException e) {
      throw new BadRequestException(
        "invalid_id",
        "The provided id is not valid."
      );
    }
  }

  @Override
  public UserValue getUserValueByUserId(String userId) {
    try {
      return useValueRepository
        .findByUserId(Long.valueOf(userId))
        .orElseThrow(() -> new UserValueNotFoundException());
    } catch (NumberFormatException e) {
      throw new BadRequestException(
        "invalid_user_id",
        "The provided user ID is not valid."
      );
    }
  }

  @Override
  public UserValue createUserValueForUser(User user) {
    UserValue userValue = UserValue
      .builder()
      .user(user)
      .createdAt(LocalDateTime.now())
      .build();

    return useValueRepository.save(userValue);
  }
}
