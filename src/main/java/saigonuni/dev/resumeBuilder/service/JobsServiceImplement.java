package saigonuni.dev.resumeBuilder.service;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Jobs;
import saigonuni.dev.resumeBuilder.domain.Plan;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.Plan.CreatePlanAdminRequest;
import saigonuni.dev.resumeBuilder.repository.JobsRepository;

@Service
@Slf4j
public class JobsServiceImplement implements JobsService {

  private final JobsRepository jobsRepo;

  @Autowired
  public JobsServiceImplement(JobsRepository jobsRepo) {
    this.jobsRepo = jobsRepo;
  }

  public Jobs addJobs(Jobs jobs) {
    Jobs job = Jobs
      .builder()
      .Company(jobs.getCompany())
      .JobId(jobs.getJobId())
      .email(jobs.getEmail())
      .status(true)
      .createdAt(LocalDateTime.now())
      .build();
    return jobsRepo.save(job);
  }
}
