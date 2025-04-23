package saigonuni.dev.resumeBuilder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;
import saigonuni.dev.resumeBuilder.message.UserSubcription;

@Service
public interface UserSubcriptionService {
  // List<UserSubscription> FetchDataUserSubWithPlan(Long userId);
  List<UserSubscription> FetchDataUserSub();
  // fetch data user subcribe follow a special user id 
  List<UserSupcriptionDTO> FetchDataUserSubWithPlanWithUser(Long userId);
  UserSubcription FetchSpecialUserSubActive(Long userId);
  UserSupcriptionDTO findUserActivateSubscription(
    Long userId,
    Boolean isActive
  );
}
