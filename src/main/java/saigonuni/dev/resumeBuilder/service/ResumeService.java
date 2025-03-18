package saigonuni.dev.resumeBuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdmin;
import saigonuni.dev.resumeBuilder.repository.EducationRepository;
import saigonuni.dev.resumeBuilder.repository.ResumeRepository;
import saigonuni.dev.resumeBuilder.repository.WorkExperienceRepository;

@Service
public interface ResumeService {
  // Resume addResume(CreateResumeAdmin request);
  Resume addResume(Resume resume);
  
}
