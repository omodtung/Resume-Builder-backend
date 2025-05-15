package saigonuni.dev.resumeBuilder.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Plan;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, String> {
  Resume save(Resume resume);
  Resume findById(Resume resume);

  @Query("SELECT r FROM Resume r WHERE r.userValue.user.id = :userId")
  List<Resume> findResumesWithUserFullyRegister(@Param("userId") Long userId);

  // @Query(
  //   "SELECT r FROM Resume r LEFT JOIN FETCH r.workExperiences LEFT JOIN FETCH r.educations"
  // )
  // List<Resume> findAllWithDetails();
  @Modifying
  @Query("UPDATE Resume r SET r.photoUrl = :photoUrl WHERE r.id = :idResume")
  void updatePhotoUrlByResumeId(
    @Param("idResume") Long idResume,
    @Param("photoUrl") String photoUrl
  );

  @Modifying
  @Query("UPDATE Resume r SET r.photoUrl = null WHERE r.id = :idResume")
  void updatePhotoUrlByResumeIdToNull(@Param("idResume") Long idResume);

  Page<Resume> findByTitleContaining(String title, Pageable pageable);

  @Query(
    "SELECT r FROM Resume r WHERE " +
    "(:column = 'title' AND LOWER(r.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
    "(:column = 'type' AND LOWER(r.type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
    "(:column = 'description' AND LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
    "(:column = 'userValue' AND LOWER(r.userValue.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')))"
  )
  Page<Resume> searchByTermAcrossFieldsWithColumn(
    @Param("searchTerm") String searchTerm,
    @Param("column") String column,
    Pageable pageable
  );
}
