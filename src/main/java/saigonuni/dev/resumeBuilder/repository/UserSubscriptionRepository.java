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
  // @Query(
  //   "SELECT us FROM UserSubscription us JOIN FETCH us.user u JOIN FETCH us.plan p WHERE u.id = :userId"
  // )
  // List<UserSupcriptionDTO> FetchDataUserSubWithPlanWithUser( // Changed return type to List<UserSubscription>
  //   @Param("userId") Long userId
  // );

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

  // @Modifying
  // @Transactional
  // @Query(
  //   "UPDATE UserSubscription r SET r.isActive = :isActive WHERE r.user.id = :idUserSub"
  // )
  // void updateActiveByUserId(
  //   @Param("idUserSub") Long idUserSub,
  //   @Param("isActive") Boolean isActive,
  //   @Param("planId") Long plan
  // );

  // to do fix in new plan back up to old plan :>>>
  // after change to new plan but regret and gonna check to ole plan
  @Modifying
  @Transactional
  @Query(
    "UPDATE UserSubscription r SET r.isActive = :isActive " +
    "WHERE r.user.id = :idUserSub " +
    "AND r.plan.id != :planId " +
    "AND (r.stripeCancelAtPeriodEnd IS NULL OR r.stripeCancelAtPeriodEnd = TRUE)"
  )
  void updateActiveByUserId(
    @Param("idUserSub") Long idUserSub,
    @Param("isActive") Boolean isActive,
    @Param("planId") Long planId
  );

  @Query(
    "SELECT us FROM UserSubscription us JOIN FETCH us.user u JOIN FETCH us.plan p " +
    "WHERE u.id = :idUserSub AND us.isActive = :isActive"
  )
UserSubscription findUserActivateSubscription(
    @Param("idUserSub") Long idUserSub,
    @Param("isActive") Boolean isActive
  );


  @Query(
    "SELECT u FROM UserSubscription u WHERE " +
    "(:column = 'stripeCustomerId' AND LOWER(u.stripeCustomerId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
    "(:column = 'stripeSubscriptionId' AND LOWER(u.stripeSubscriptionId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
    "(:column = 'user' AND LOWER(u.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) OR " +
    "(:column = 'plan' AND LOWER(u.plan.plansName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))"
)
Page<UserSubscription> findByTermAcrossFieldsWithColumn(
    @Param("searchTerm") String searchTerm,
    @Param("column") String column,
    Pageable pageable
);

}
