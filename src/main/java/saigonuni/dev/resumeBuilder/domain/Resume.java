package saigonuni.dev.resumeBuilder.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resumes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resume implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private String photoUrl;

  @Column(nullable = false)
  private String colorHex = "#000000";

  @Column(nullable = true)
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
    orphanRemoval = true,
    cascade = CascadeType.ALL
  )
  @JsonManagedReference
  private List<WorkExperience> workExperiences;

  @OneToMany(
    mappedBy = "resume",
    orphanRemoval = true,
    cascade = CascadeType.ALL
  )
  @JsonManagedReference
  private List<Education> educations;

  @ElementCollection
  private List<String> skills;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = true)
  private LocalDateTime updatedAt = LocalDateTime.now();

  public Resume(
    Long id,
    String title,
    String description,
    String photoUrl,
    String colorHex,
    String borderStyle,
    String summary,
    String firstName,
    String lastName,
    String jobTitle,
    String city,
    String country,
    String phone,
    String email,
    List<WorkExperience> workExperiences,
    List<Education> educations,
    List<String> skills
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.photoUrl = photoUrl;
    this.colorHex = colorHex;
    this.borderStyle = borderStyle;
    this.summary = summary;
    this.firstName = firstName;
    this.lastName = lastName;
    this.jobTitle = jobTitle;
    this.city = city;
    this.country = country;
    this.phone = phone;
    this.email = email;

    this.workExperiences = workExperiences;
    this.educations = educations;
    this.skills = skills;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getColorHex() {
    return colorHex;
  }

  public void setColorHex(String colorHex) {
    this.colorHex = colorHex;
  }

  public String getBorderStyle() {
    return borderStyle;
  }

  public void setBorderStyle(String borderStyle) {
    this.borderStyle = borderStyle;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<WorkExperience> getWorkExperiences() {
    return workExperiences;
  }

  public void setWorkExperiences(List<WorkExperience> workExperiences) {
    this.workExperiences = workExperiences;
  }

  public List<Education> getEducations() {
    return educations;
  }

  public void setEducations(List<Education> educations) {
    this.educations = educations;
  }

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> skills) {
    this.skills = skills;
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
