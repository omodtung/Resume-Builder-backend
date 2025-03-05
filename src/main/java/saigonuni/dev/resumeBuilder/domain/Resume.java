package saigonuni.dev.resumeBuilder.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "resumes")
public class Resume {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column(nullable = false)
  private String userId;

  private String title;
  private String description;
  private String photoUrl;

  @Column(nullable = false)
  private String colorHex = "#000000";

  @Column(nullable = false)
  private String borderStyle = "squircle";

  private String summary;
  private String firstName;
  private String lastName;
  private String jobTitle;
  private String city;
  private String country;
  private String phone;
  private String email;

  @OneToMany(
    mappedBy = "resume",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<WorkExperience> workExperiences;

  @OneToMany(
    mappedBy = "resume",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<Education> educations;

  @ElementCollection
  private List<String> skills;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PreUpdate
  public void setLastUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
  // Getters and setters
}
