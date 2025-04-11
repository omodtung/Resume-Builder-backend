package saigonuni.dev.resumeBuilder.service;

import com.fasterxml.jackson.annotation.OptBoolean;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Plan;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.Plan.CreatePlanAdminRequest;
import saigonuni.dev.resumeBuilder.dto.Plan.UpdatePlanAdminRequest;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;
import saigonuni.dev.resumeBuilder.message.CommonMessage;
import saigonuni.dev.resumeBuilder.message.PlanMessage;
import saigonuni.dev.resumeBuilder.repository.PlanRepository;

@Service
@Slf4j
public class PlanServiceImplement implements PlanService {

  private final PlanRepository planRepository;

  @Autowired
  public PlanServiceImplement(PlanRepository planRepository) {
    this.planRepository = planRepository;
  }

  @Override
  public Plan addPlan(CreatePlanAdminRequest request, User user) {
    try {
      Plan existingPlan = planRepository.findByPlansName(
        request.getPlansName()
      );

      if (!existingPlan.equals(null)) {
        throw new BadRequestException(
          PlanMessage.PLAN_NAME_EXIST_KEY,
          PlanMessage.PLAN_NAME_EXIST_MESSAGE
        );
      }
      Plan plan = Plan
        .builder()
        .plansName(request.getPlansName())
        .Description(request.getDescription())
        .price(request.getPrice())
        .build();
      return planRepository.save(plan);
    } catch (Exception e) {
      throw new RuntimeException(e); // Wrap the original exception
    }
  }

  @Override
  public Plan getPlanById(Long id) {
    return planRepository
      .findById(id)
      .orElseThrow(() ->
        new BadRequestException(
          PlanMessage.PLAN_NOT_FOUND_KEY,
          PlanMessage.PLAN_NOT_FOUND_MESSAGE
        )
      );
  }

  @Override
  public List<Plan> listPlans() {
    return planRepository.findAll();
  }

  @Override
  public Plan updatePlan(Long id, UpdatePlanAdminRequest request, User user) {
    try {
      Plan plan = planRepository
        .findById(id)
        .orElseThrow(() ->
          new BadRequestException(
            PlanMessage.PLAN_NOT_FOUND_KEY,
            PlanMessage.PLAN_NOT_FOUND_MESSAGE
          )
        );
      plan.setPlansName(request.getPlansName());
      plan.setDescription(request.getDescription());
      plan.setPrice(request.getPrice());
      return planRepository.save(plan);
    } catch (Exception e) {
      throw new RuntimeException(e); // Wrap the original exception
    }
  }

  // @Override
  // public void deletePlan(Long id) {
  //   try {
  //     Plan plan = planRepository
  //       .findById(id)
  //       .orElseThrow(() ->
  //         new BadRequestException(
  //           PlanMessage.PLAN_NOT_FOUND_KEY,
  //           PlanMessage.PLAN_NOT_FOUND_MESSAGE
  //         )
  //       );
  //     planRepository.delete(plan);
  //   } catch (Exception e) {
  //     throw new RuntimeException(e); // Wrap the original exception
  //   }
  // }
}
