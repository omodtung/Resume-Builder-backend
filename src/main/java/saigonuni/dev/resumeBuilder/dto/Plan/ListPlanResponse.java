package saigonuni.dev.resumeBuilder.dto.Plan;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import saigonuni.dev.resumeBuilder.domain.Plan;

@Data
@Builder
public class ListPlanResponse {
    private List<Plan> plans;
}
