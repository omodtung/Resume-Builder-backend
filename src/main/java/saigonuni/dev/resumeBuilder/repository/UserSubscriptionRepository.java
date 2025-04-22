package saigonuni.dev.resumeBuilder.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Changed import
import org.springframework.data.jpa.repository.Query; // Changed import
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;

@Repository
public interface UserSubscriptionRepository
  extends JpaRepository<UserSubscription, Long> {
  Optional<UserSubscription> findByUserId(Long userId);

  Optional<UserSubscription> findByStripeCustomerId(String stripeCustomerId);

  Optional<UserSubscription> findByStripeSubscriptionId(
    String stripeSubscriptionId
  );

  @Modifying // Mark this method as one that modifies data (DELETE or UPDATE)
  @Transactional // Ensure this operation runs within a transaction
  @Query(
    "DELETE FROM UserSubscription us WHERE us.stripeSubscriptionId = :stripeSubscriptionId"
  )
  long deleteByStripeSubscriptionId(String stripeSubscriptionId);

  // fetch User + Plan Support Checking The Plan Special
  // @Query("SELECT us FROM UserSubscription us JOIN us.plan ")
  // Removed @Modifying as this is a SELECT query
  @Query(
    "SELECT us FROM UserSubscription us JOIN FETCH us.user u JOIN FETCH us.plan p WHERE u.id = :userId"
  )
  List<UserSubscription> FetchDataUserSubWithPlanWithUser( // Changed return type to List<UserSubscription>
    @Param("userId") Long userId
  );

  @Modifying
  @Query(
    "SELECT us FROM UserSubscription us JOIN FETCH us.user u JOIN FETCH us.plan p"
  )
  List<UserSubscription> FetchDataUserSub();
}
