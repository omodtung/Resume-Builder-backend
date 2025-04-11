package saigonuni.dev.resumeBuilder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscriptions")
public class UserSubscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String stripeCustomerId;

  @Column(nullable = false, unique = true)
  private String stripeSubscriptionId;

  // @Column(nullable = false)
  // private String stripePriceId;

  @Column(nullable = false)
  private LocalDateTime stripeCurrentPeriodEnd;

  @Column(nullable = false)
  private Boolean stripeCancelAtPeriodEnd = false;

  // cho phep User cam khoa chinh cua UserSubscription = mappedBy
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  // @JsonBackReference
  private User user;

  @ManyToOne
  @JoinColumn(name = "stripePriceId", nullable = false)
  @JsonIgnore
  private Plan plan;

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
