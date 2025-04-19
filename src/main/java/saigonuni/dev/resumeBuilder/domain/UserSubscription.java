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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_subscriptions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public LocalDateTime getStripeCurrentPeriodEnd() {
        return stripeCurrentPeriodEnd;
    }

    public void setStripeCurrentPeriodEnd(LocalDateTime stripeCurrentPeriodEnd) {
        this.stripeCurrentPeriodEnd = stripeCurrentPeriodEnd;
    }

    public Boolean getStripeCancelAtPeriodEnd() {
        return stripeCancelAtPeriodEnd;
    }

    public void setStripeCancelAtPeriodEnd(Boolean stripeCancelAtPeriodEnd) {
        this.stripeCancelAtPeriodEnd = stripeCancelAtPeriodEnd;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
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
