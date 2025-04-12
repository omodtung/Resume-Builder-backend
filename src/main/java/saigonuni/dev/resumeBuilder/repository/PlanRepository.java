package saigonuni.dev.resumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import saigonuni.dev.resumeBuilder.domain.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
  @Query(
    value = "SELECT * FROM plan WHERE plans_name = :plansName",
    nativeQuery = true
  )
  Plan findByPlansName(@Param("plansName") String plansName);
}
