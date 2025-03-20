package saigonuni.dev.resumeBuilder.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.exception.ResumeNotFoundException;
import saigonuni.dev.resumeBuilder.repository.EducationRepository;
import saigonuni.dev.resumeBuilder.repository.ResumeRepository;
import saigonuni.dev.resumeBuilder.repository.WorkExperienceRepository;

@Service
@Slf4j
public class ResumeServiceImplement implements ResumeService {

  @Autowired
  private ResumeRepository resumeRepository;

  @Autowired
  private WorkExperienceRepository workExperienceRepository;

  @Autowired
  private EducationRepository educationRepository;

  @Override
  public Resume addResume(CreateResumeAdminRequest request) {
    Resume resume = Resume
      .builder()
      .title(request.getTitle())
      .colorHex(request.getColorHex())
      .borderStyle(request.getBorderStyle())
      .createdAt(LocalDateTime.now())
      .description(request.getDescription())
      .photoUrl(request.getPhotoUrl())
      .summary(request.getSummary())
      .firstName(request.getFirstName())
      .lastName(request.getLastName())
      .city(request.getCity())
      .country(request.getCountry())
      .phone(request.getPhone())
      .email(request.getEmail())
      .workExperiences(request.getWorkExperiences())
      .educations(request.getEducations())
      .skills(request.getSkills())
      .build();

    Resume savedResume = resumeRepository.save(resume);

    for (WorkExperience workExperience : resume.getWorkExperiences()) {
      workExperience.setResume(savedResume);
    }
    for (Education education : resume.getEducations()) {
      education.setResume(savedResume);
    }
    workExperienceRepository.saveAll(resume.getWorkExperiences());
    educationRepository.saveAll(resume.getEducations());

    return savedResume;
  }

  @Override
  public Resume getResumeById(String id) {
    Optional<Resume> OptionalResume = resumeRepository.findById(id);
    return OptionalResume.orElseThrow(() -> new ResumeNotFoundException());
  }

  @Override
  public List<Resume> listResumes() {
    return resumeRepository.findAll();
  }
}
