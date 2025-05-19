package saigonuni.dev.resumeBuilder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
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
@Table(name = "educations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Education {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String degree;
  private String school;
  private LocalDate startDate;
  private LocalDate endDate;

  // test
  // @ManyToOne
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resume_id", nullable = false)
  @JsonBackReference
  private Resume resume;

  // @Column(nullable = false, updatable = false)
  // private LocalDateTime createdAt = LocalDateTime.now();

  // @Column(nullable = true)
  // private LocalDateTime updatedAt = LocalDateTime.now();

  @CreationTimestamp // Hibernate will set this on INSERT
  @Column(name = "created_at", nullable = false, updatable = false) // Match column name, ensure NOT NULL, cannot be updated later
  private LocalDateTime createdAt; // Use LocalDateTime or Instant or Date

  // @Column(nullable = true)
  // private LocalDateTime updatedAt = LocalDateTime.now();
  @UpdateTimestamp // Hibernate will set this on INSERT and UPDATE
  @Column(name = "updated_at", nullable = false) // Match column name, ensure NOT NULL
  private LocalDateTime updatedAt; // Use LocalDateTime or Instant or Date

  @PreUpdate
  public void setLastUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  @JsonProperty("resume_id")
  public Long getResumeIdForSerialization() {
    if (this.resume != null) {
      return this.resume.getId();
    }
    return null;
  }

  public Education(
    String degree,
    String school,
    LocalDate startDate,
    LocalDate endDate
  ) {
    this.degree = degree;
    this.school = school;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school = school;
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
