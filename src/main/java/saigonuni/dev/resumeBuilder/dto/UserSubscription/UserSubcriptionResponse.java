package saigonuni.dev.resumeBuilder.dto.UserSubscription;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubcriptionResponse {

  private List<UserSubscription> userSubscription;
}
