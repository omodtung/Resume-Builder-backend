package saigonuni.dev.resumeBuilder.dto.UserValue;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;

@Data
@Builder
public class CreateUserValueRequest {

    private User user;
// //   private Long userId;
//   private List<Resume> resume;
}
