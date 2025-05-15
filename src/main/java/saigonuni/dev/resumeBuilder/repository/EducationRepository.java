package saigonuni.dev.resumeBuilder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
  Education save(Education education);
    
  @Query(
    "SELECT p FROM Education p WHERE " +
    "CASE " +
    "WHEN :column = 'degree' THEN LOWER(p.degree) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "WHEN :column = 'school' THEN LOWER(p.school) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "WHEN :column = 'startDate' THEN CAST(p.startDate AS string) LIKE CONCAT('%', :searchTerm, '%') " +
    "WHEN :column = 'endDate' THEN CAST(p.endDate AS string) LIKE CONCAT('%', :searchTerm, '%') " +
    "ELSE FALSE END"
  )
  Page<Education> searchByTermAcrossFieldsWithColumm(
    @Param("searchTerm") String searchTerm,
    @Param("column") String column,
    Pageable pageable
  );
}
