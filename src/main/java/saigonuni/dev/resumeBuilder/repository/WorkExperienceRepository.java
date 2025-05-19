package saigonuni.dev.resumeBuilder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.dto.workExperience.WorkExperienceDomainResponse;

@Repository
public interface WorkExperienceRepository
  extends JpaRepository<WorkExperience, Long> {
  WorkExperience save(WorkExperience workExperience);

  @Query(
    "SELECT p FROM WorkExperience p LEFT JOIN FETCH p.resume WHERE " +
    "CASE " +
    "WHEN :column = 'position' THEN LOWER(p.position) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "WHEN :column = 'company' THEN LOWER(p.company) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "WHEN :column = 'description' THEN LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "ELSE FALSE END"
  )
  Page<WorkExperience> searchByTermAcrossFieldsWithColumm(
    @Param("searchTerm") String searchTerm,
    @Param("column") String column,
    Pageable pageable
  );

  @Query(
    value = "SELECT w FROM WorkExperience w LEFT JOIN FETCH w.resume", // JOIN FETCH để tải resume
    countQuery = "SELECT count(w) FROM WorkExperience w"
  ) // Cần countQuery cho phân trang
  Page<WorkExperience> findAllFetchingResume(Pageable pageable);
}
