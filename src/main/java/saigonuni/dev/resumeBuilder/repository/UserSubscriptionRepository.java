package saigonuni.dev.resumeBuilder.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;

@Repository
public interface UserSubscriptionRepository
  extends JpaRepository<UserSubscription, Long> {
  Optional<UserSubscription> findByUserId(Long userId);

  Optional<UserSubscription> findByStripeCustomerId(String stripeCustomerId);

  Optional<UserSubscription> findByStripeSubscriptionId(String stripeSubscriptionId);

  @Modifying // Mark this method as one that modifies data (DELETE or UPDATE)
  @Transactional // Ensure this operation runs within a transaction
  @Query("DELETE FROM UserSubscription us WHERE us.stripeSubscriptionId = :stripeSubscriptionId")
  long deleteByStripeSubscriptionId(String stripeSubscriptionId);

}

