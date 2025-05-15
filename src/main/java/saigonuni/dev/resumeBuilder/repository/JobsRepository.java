package saigonuni.dev.resumeBuilder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Jobs;
import saigonuni.dev.resumeBuilder.domain.Plan;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, Long> {
  Jobs save(Jobs jobs);
  @Query(
    "SELECT p FROM Jobs p WHERE " +
    "CASE " +
    "WHEN :column = 'email' THEN LOWER(p.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "WHEN :column = 'status' THEN p.status = CASE WHEN :searchTerm = 'true' THEN true WHEN :searchTerm = 'false' THEN false ELSE p.status END " +
    "WHEN :column = 'Company' THEN LOWER(p.Company) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "WHEN :column = 'createdAt' THEN CAST(p.createdAt AS string) LIKE CONCAT('%', :searchTerm, '%') " +
    "ELSE FALSE END"
  )
  Page<Jobs> searchByTermAcrossFieldsWithColumm(
    @Param("searchTerm") String searchTerm,
    @Param("column") String column,
    Pageable pageable
  );
}
