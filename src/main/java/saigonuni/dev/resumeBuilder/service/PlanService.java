package saigonuni.dev.resumeBuilder.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import saigonuni.dev.resumeBuilder.domain.Plan;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.Plan.CreatePlanAdminRequest;
import saigonuni.dev.resumeBuilder.dto.Plan.UpdatePlanAdminRequest;

public interface PlanService {
  Plan addPlan(CreatePlanAdminRequest request, User user);
  Plan getPlanById(Long id);
  List<Plan> listPlans();
  Plan updatePlan(Long id, UpdatePlanAdminRequest request, User user);
  Plan findStripePriceByPlanId(Long PlanId);

  public  Page<Plan> listPlanswithfilter(
    String searchTerm,
    String column,
    Pageable pageable
  );
  // void deletePlan(Long id);
}
