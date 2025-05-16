package saigonuni.dev.resumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Data;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
  boolean existsByIdResume(String idResume);
}
