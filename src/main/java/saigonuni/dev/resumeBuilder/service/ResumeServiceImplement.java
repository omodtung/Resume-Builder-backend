package saigonuni.dev.resumeBuilder.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.domain.dto.EducationDTO;
import saigonuni.dev.resumeBuilder.domain.dto.WorkExperienceDTO;
import saigonuni.dev.resumeBuilder.dto.UserValue.CreateUserValueRequest;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.exception.ResumeNotFoundException;
import saigonuni.dev.resumeBuilder.repository.EducationRepository;
import saigonuni.dev.resumeBuilder.repository.ResumeRepository;
import saigonuni.dev.resumeBuilder.repository.UserRepository;
import saigonuni.dev.resumeBuilder.repository.UserValueRepository;
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

  @Autowired
  private UserValueRepository userValueRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserValueService userValueService;

  @Override
  public Resume addResume(CreateResumeAdminRequest request, User user) {
    UserValue userValue = userValueRepository.save(
      UserValue.builder().user(user).createdAt(LocalDateTime.now()).build()
    );

    List<WorkExperience> workExperienceEntities = new ArrayList<>();
    if (request.getWorkExperiences() != null) {
      for (WorkExperienceDTO dto : request.getWorkExperiences()) {
        WorkExperience workExperience = WorkExperience
          .builder()
          .position(dto.getPosition())
          .company(dto.getCompany())
          .startDate(dto.getStartDate())
          .endDate(dto.getEndDate())
          .description(dto.getDescription())
          .build();
        workExperienceEntities.add(workExperience);
      }
    }

    List<Education> educationEntities = new ArrayList<>();
    if (request.getEducations() != null) {
      for (EducationDTO dto : request.getEducations()) {
        Education education = Education
          .builder()
          .degree(dto.getDegree())
          .school(dto.getSchool())
          .startDate(dto.getStartDate())
          .endDate(dto.getEndDate())
          .build();
        educationEntities.add(education);
      }
    }
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
      .skills(request.getSkills())
      .userValue(userValue)
      .build();

    Resume savedResume = resumeRepository.save(resume);

    for (WorkExperience workExperience : workExperienceEntities) {
      workExperience.setResume(savedResume);
    }
    for (Education education : educationEntities) {
      education.setResume(savedResume);
    }
    if (!workExperienceEntities.isEmpty()) {
      workExperienceRepository.saveAll(workExperienceEntities);
    }
    if (!educationEntities.isEmpty()) {
      educationRepository.saveAll(educationEntities);
    }

    savedResume.setWorkExperiences(workExperienceEntities);
    savedResume.setEducations(educationEntities);

    // CreateUserValueRequest createUserValueRequest = CreateUserValueRequest
    //   .builder()
    //   .resume(Collections.singletonList(savedResume))
    //   .user(user)
    //   .build();
    // userValueService.save(createUserValueRequest);
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

  @Override
  public void deleteResume(String id) {
    Resume resume = resumeRepository
      .findById(id)
      .orElseThrow(ResumeNotFoundException::new);
    resumeRepository.delete(resume);
  }

  // TODO : Fix this Update have Error 500 internal server error
  @Override
  public Resume updateResume(
    String resumeId,
    UpdateResumeAdminRequest request
  ) {
    Resume resume = resumeRepository
      .findById(resumeId)
      .orElseThrow(ResumeNotFoundException::new);
    // resume.setTitle(request.getTitle());
    resume.setTitle(request.getTitle());

    resume.setColorHex(request.getColorHex());
    resume.setBorderStyle(request.getBorderStyle());
    resume.setDescription(request.getDescription());
    resume.setPhotoUrl(request.getPhotoUrl());
    resume.setSummary(request.getSummary());
    resume.setFirstName(request.getFirstName());
    resume.setLastName(request.getLastName());
    resume.setCity(request.getCity());
    resume.setCountry(request.getCountry());
    resume.setPhone(request.getPhone());
    resume.setEmail(request.getEmail());
    resume.setWorkExperiences(request.getWorkExperiences());
    resume.setEducations(request.getEducations());
    resume.setSkills(request.getSkills());

    System.err.println("resume Input " + resume);
    Resume savedResume = resumeRepository.save(resume);

    if (request.getWorkExperiences() != null) {
      for (WorkExperience workExperience : request.getWorkExperiences()) {
        workExperience.setResume(savedResume);
      }
      workExperienceRepository.saveAll(request.getWorkExperiences());
    }

    if (request.getEducations() != null) {
      for (Education education : request.getEducations()) {
        education.setResume(savedResume);
      }
      educationRepository.saveAll(request.getEducations());
    }

    return savedResume;
  }
}
