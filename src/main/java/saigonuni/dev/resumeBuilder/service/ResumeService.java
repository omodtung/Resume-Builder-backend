package saigonuni.dev.resumeBuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.repository.EducationRepository;
import saigonuni.dev.resumeBuilder.repository.ResumeRepository;
import saigonuni.dev.resumeBuilder.repository.WorkExperienceRepository;

@Service
public class ResumeService {

  private final ResumeRepository resumeRepository;
  private final WorkExperienceRepository workExperienceRepository;
  private final EducationRepository educationRepository;

  @Autowired
  public ResumeService(ResumeRepository resumeRepository , WorkExperienceRepository workExperienceRepository , EducationRepository educationRepository
  ) {
    this.resumeRepository = resumeRepository;
    this.educationRepository = educationRepository;
    this.workExperienceRepository = workExperienceRepository;
  }

  public Resume handleSaveResume(Resume resume) {
    return resumeRepository.save(resume);
  }

  public Resume addResume(Resume resume) {
  
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
