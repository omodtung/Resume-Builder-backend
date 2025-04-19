package saigonuni.dev.resumeBuilder.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;

@Repository
public interface UserSubscriptionRepository
  extends JpaRepository<UserSubscription, Long> {
  Optional<UserSubscription> findByUserId(Long userId);

  Optional<UserSubscription> findByStripeCustomerId(String stripeCustomerId);

  Optional<UserSubscription> findByStripeSubscriptionId(String stripeSubscriptionId);

  void deleteByStripeCustomerId(String stripeCustomerId);

}

