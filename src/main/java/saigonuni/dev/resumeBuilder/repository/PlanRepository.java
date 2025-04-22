package saigonuni.dev.resumeBuilder.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  

  @Query(
    "SELECT p FROM Plan p WHERE " +
    "LOWER(p.plansName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(p.Description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(p.price) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(p.stripePriceId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
  )
  Page<Plan> searchByTermAcrossFields(
    @Param("searchTerm") String searchTerm,
    Pageable pageable
  );



  @Query(
    "SELECT p FROM Plan p WHERE " +
    "LOWER(p.plansName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(p.Description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(p.price) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(p.stripePriceId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
  )
  Page<Plan> searchByTermAcrossFieldsWithColumn(
    @Param("searchTerm") String searchTerm,   @Param("column") String column
    Pageable pageable
  );
}
