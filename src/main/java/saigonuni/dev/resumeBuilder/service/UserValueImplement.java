package saigonuni.dev.resumeBuilder.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import saigonuni.dev.resumeBuilder.domain.UserValue;
import saigonuni.dev.resumeBuilder.dto.UserValue.CreateUserValueRequest;
import saigonuni.dev.resumeBuilder.exception.DuplicateKeyException;
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
        .resume(userValue.getResume())
        .user(userValue.getUser())
        
        .build();

      return useValueRepository.save(userValues);
    } catch (DataIntegrityViolationException e) {
      throw new DuplicateKeyException(CommonMessage.DUPLICATE_KEY.getMessage());
    }
  }
}
