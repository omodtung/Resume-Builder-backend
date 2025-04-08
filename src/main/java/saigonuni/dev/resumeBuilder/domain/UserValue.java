package saigonuni.dev.resumeBuilder.domain;

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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_values")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserValue {

  //   @OneToOne(
  //     mappedBy = "user_id",
  //     cascade = CascadeType.ALL,
  //     orphanRemoval = true
  //   )
  // private User user;
  //    this is view in table UserValue
  // UserValue Table (One-to-Many relationship with User)
  // id (PK)	user_id (FK to User.id)	value
  // 1	1	100
  // 2	1	200
  // 3	2	300

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = true)
  private LocalDateTime updatedAt;

  @Column(nullable = true)
  private LocalDateTime deletedAt;

  //   solution 2
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = true)
  @JsonManagedReference
  private User user;

  // mappedBy = "user" chỉ ra rằng Resume.user là cột chứa khóa ngoại (user_id) trong bảng resumes.
  // Hibernate không tạo bảng trung gian vì Resume đã có cột user_id để liên kết với User.
  // Khi một User bị xóa, toàn bộ Resume của người đó cũng bị xóa (CascadeType.ALL + orphanRemoval = true).
  @OneToMany(
    mappedBy = "userValue",
    // mappedBy Cho phep Resume cam khoa chinh cua UserValue
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @JsonManagedReference
  private List<Resume> resume = new ArrayList<>();

  // @ManyToOne
  // @JoinColumn(name = "user_subcription_id", nullable = true)
  // @JsonManagedReference
  // private UserSubscription userSubscription;

  @Override
  public String toString() {
    return (
      "UserValue{" +
      "id=" +
      id +
      ", createdAt=" +
      createdAt +
      ", updatedAt=" +
      updatedAt +
      '}'
    );
  }
}
