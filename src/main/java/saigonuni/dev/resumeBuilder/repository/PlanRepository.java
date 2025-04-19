package saigonuni.dev.resumeBuilder.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import saigonuni.dev.resumeBuilder.domain.Plan;
import saigonuni.dev.resumeBuilder.domain.Resume;

public interface PlanRepository extends JpaRepository<Plan, Long> {
  @Query(
    value = "SELECT * FROM plan WHERE plans_name = :plansName",
    nativeQuery = true
  )
  Plan findByPlansName(@Param("plansName") String plansName);

  @Query(value = "SELECT * FROM plan WHERE id = :id", nativeQuery = true)
  Plan findStripePriceIByPlanName(@Param("id") Long id);

  @Query(
    value = "SELECT p.id, p.stripePriceId, p.plansName, p.Description, p.price, p.created_at, p.updated_at " + // Liệt kê các cột cần thiết
    "FROM plan p WHERE p.id = :id",
    nativeQuery = true
  )
  Plan findSpecificPlanByIdNative(@Param("id") Long id);

  Optional<Plan> findByStripePriceId(String stripePriceId);
}
