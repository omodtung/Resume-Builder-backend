package saigonuni.dev.resumeBuilder.dto.Plan;

import lombok.Builder;
import lombok.Data;
import saigonuni.dev.resumeBuilder.domain.Plan;

@Data
@Builder
public class CreatePlanAdminResponse {
    private Plan plan;
}
