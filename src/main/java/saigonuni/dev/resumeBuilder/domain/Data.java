package saigonuni.dev.resumeBuilder.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "data-skill-train")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Data {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "skills_text", columnDefinition = "TEXT")
  private String skill;

  @Column(name = "company_type") //
  private String company;

  @Column(name = "resume_source_id", unique = true)
  private String idResume;
}
