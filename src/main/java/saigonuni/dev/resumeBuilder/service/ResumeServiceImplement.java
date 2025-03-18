package saigonuni.dev.resumeBuilder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.repository.EducationRepository;
import saigonuni.dev.resumeBuilder.repository.ResumeRepository;
import saigonuni.dev.resumeBuilder.repository.WorkExperienceRepository;
import saigonuni.dev.resumeBuilder.service.*;

@Service
@Slf4j
public class ResumeServiceImplement implements ResumeService {

  @Autowired
  private ResumeRepository resumeRepository;

  @Autowired
  private WorkExperienceRepository workExperienceRepository;

  @Autowired
  private EducationRepository educationRepository;

  //   @Autowired
  //   public ResumeServiceImplement implement ResumeService(
  //     ResumeRepository resumeRepository,
  //     WorkExperienceRepository workExperienceRepository,
  //     EducationRepository educationRepository
  //   ) {
  //     this.resumeRepository = resumeRepository;
  //     this.educationRepository = educationRepository;
  //     this.workExperienceRepository = workExperienceRepository;
  //   }

  public Resume handleSaveResume(Resume resume) {
    return resumeRepository.save(resume);
  }

  public Resume addResume(Resume resume) {
    // log.info("addResume: {}", resume);
    // return resumeRepository.save(resume);
    Resume savedResume = resumeRepository.save(resume);

    // Gán resume cho các workExperiences và educations
    for (WorkExperience workExperience : resume.getWorkExperiences()) {
      workExperience.setResume(savedResume);
    }
    for (Education education : resume.getEducations()) {
      education.setResume(savedResume);
    }

    // Sau khi gán xong resume, lưu các workExperiences và educations
    workExperienceRepository.saveAll(resume.getWorkExperiences());
    educationRepository.saveAll(resume.getEducations());

    return savedResume;
  }
}
