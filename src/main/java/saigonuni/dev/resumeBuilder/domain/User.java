package saigonuni.dev.resumeBuilder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String role;

  @Column(name = "refresh_token", nullable = true)
  private String refreshToken;

  // mappedBy = "user" chỉ ra rằng Resume.user là cột chứa khóa ngoại (user_id) trong bảng resumes.
  // Hibernate không tạo bảng trung gian vì Resume đã có cột user_id để liên kết với User.
  // Khi một User bị xóa, toàn bộ Resume của người đó cũng bị xóa (CascadeType.ALL + orphanRemoval = true).
  // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  // private List<Resume> resume;

  // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  // private UserSubscription userSubscription;

  // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  // private UserValue userValue;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  // @JsonBackReference
  @JsonManagedReference
  private List<UserValue> userValues;

  // @ManyToOne
  // @JoinColumn(name = "user_subscription_id", nullable = true)
  // //   @JsonBackReference
  // @JsonIgnore
  // private UserSubscription userSubscription;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  // @JsonBackReference
  private List<UserSubscription> userSubscriptions;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = true)
  private LocalDateTime updatedAt;

  @PreUpdate
  public void setLastUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public User(String username, String password, String email, String role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Return the role as a GrantedAuthority
    return Collections.singletonList(new SimpleGrantedAuthority(role));
  }

  // Getters and setters

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return (
      "User{" +
      "id=" +
      id +
      ", username='" +
      username +
      '\'' +
      ", email='" +
      email +
      '\'' +
      ", role='" +
      role +
      '\'' +
      '}'
    );
  }
}
