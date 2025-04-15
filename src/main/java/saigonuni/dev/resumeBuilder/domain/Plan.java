package saigonuni.dev.resumeBuilder.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plans")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

  private String stripePriceId;

  private String plansName;

  private String Description;

  private String price;

  @OneToMany(mappedBy = "plan", orphanRemoval = true)
  private List<UserSubscription> userSubscriptions;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = true)
  private LocalDateTime updatedAt;

  @Column(nullable = true)
  private LocalDateTime deletedAt;
}
// stripePriceId: ID của Mức giá (Price) trên Stripe. Điều này cho biết người dùng đang đăng ký gói cước cụ thể nào (ví dụ: price_pro_monthly, price_basic_annual).
