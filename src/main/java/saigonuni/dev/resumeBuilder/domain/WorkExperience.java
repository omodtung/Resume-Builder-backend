package saigonuni.dev.resumeBuilder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "work_experiences")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkExperience {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String position;
  private String company;
  private LocalDate startDate;
  private LocalDate endDate;
  private String description;

  @ManyToOne
  @JoinColumn(name = "resume_id", nullable = false)
  @JsonBackReference
  private Resume resume;

  // @Column(nullable = false, updatable = false)
  // private LocalDateTime createdAt = LocalDateTime.now();

  @CreationTimestamp // Hibernate will set this on INSERT
  @Column(name = "created_at", nullable = false, updatable = false) // Match column name, ensure NOT NULL, cannot be updated later
  private LocalDateTime createdAt; // Use LocalDateTime or Instant or Date

  // @Column(nullable = true)
  // private LocalDateTime updatedAt = LocalDateTime.now();
  @UpdateTimestamp // Hibernate will set this on INSERT and UPDATE
  @Column(name = "updated_at", nullable = false) // Match column name, ensure NOT NULL
  private LocalDateTime updatedAt; // Use LocalDateTime or Instant or Date

  // Getters and setters

  public WorkExperience(
    String position,
    String company,
    LocalDate startDate,
    LocalDate endDate,
    String description,
    Resume resume
  ) {
    this.position = position;
    this.company = company;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
    this.resume = resume;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Resume getResume() {
    return resume;
  }

  public void setResume(Resume resume) {
    this.resume = resume;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
