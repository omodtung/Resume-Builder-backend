package saigonuni.dev.resumeBuilder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Jobs;

@Service
public interface JobsService {
  Jobs addJobs(Jobs job);
}
