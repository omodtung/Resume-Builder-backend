package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;

@Service
public interface UserSubcriptionService {
  // List<UserSubscription> FetchDataUserSubWithPlan(Long userId);
  List<UserSubscription> FetchDataUserSub();
  List<UserSupcriptionDTO> FetchDataUserSubWithPlanWithUser(Long userId);
}
