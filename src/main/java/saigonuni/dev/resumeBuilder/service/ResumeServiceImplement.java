package saigonuni.dev.resumeBuilder.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import saigonuni.dev.resumeBuilder.domain.Education;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserValue;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.domain.dto.EducationDTO;
import saigonuni.dev.resumeBuilder.domain.dto.WorkExperienceDTO;
import saigonuni.dev.resumeBuilder.dto.Education.EducationResumeEdit;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.EditResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.workExperience.workExperienceResumeEditRequestDTO;
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

  @Autowired
  private UploadServiceImplement uploadServiceImplement;

  @PersistenceContext // Inject EntityManager
  private EntityManager entityManager;

  @Override
  public Resume addResume(
    CreateResumeAdminRequest request,
    User user,
    MultipartFile file
  ) {
    String avatar = "";
    if (file != null && !file.isEmpty()) {
      try {
        avatar =
          this.uploadServiceImplement.handleSaveUpLoadFile(file, "avatar");
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }

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
      .photoUrl(avatar)
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
    return OptionalResume.orElseThrow(() ->
      new ResumeNotFoundException("Resume not found with id: " + id)
    );
  }

  // @Override
  // public List<Resume> listResumes() {
  //   return resumeRepository.findAll();
  // }

  @Override
  @SuppressWarnings("unchecked") // Cần thiết vì createNativeQuery trả về List không định kiểu
  public List<Resume> listResumes() {
    log.info("Executing native query to list resumes with details"); // Thêm log nếu muốn

    // --- Câu SQL Native của bạn ---
    String sql =
      """
          SELECT
              r.*  -- Chỉ chọn các cột từ bảng resumes. JPA sẽ dùng chúng để tạo đối tượng Resume.
          FROM
              public.users_values uv
          LEFT JOIN
              public.resumes r ON uv.id = r.user_value_id -- JOIN để liên kết
          LEFT JOIN
              public.users u ON uv.user_id = u.id      -- JOIN để có thể lọc hoặc sắp xếp (nếu cần)
          WHERE
              r.id IS NOT NULL -- Chỉ lấy những dòng có thông tin Resume (do dùng LEFT JOIN từ users_values)
          ORDER BY
              uv.id ASC, -- Sắp xếp theo thứ tự mong muốn
              r.id ASC,
              u.id ASC
          """;

    try {
      Query query = entityManager.createNativeQuery(sql, Resume.class);

      List<Resume> resumes = query.getResultList();
      log.info(
        "Successfully fetched {} resumes using native query",
        resumes.size()
      );
      return resumes;
    } catch (Exception e) {
      // Bắt các exception có thể xảy ra từ query (ví dụ: PersistenceException)
      log.error(
        "Error executing native query for listing resumes: {}",
        e.getMessage(),
        e
      );
      // Có thể throw một exception tùy chỉnh hoặc trả về danh sách rỗng/null tùy logic ứng dụng
      throw new RuntimeException("Failed to list resumes", e); // Ví dụ: Ném lại runtime exception
      // Hoặc return Collections.emptyList();
    }
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
    UpdateResumeAdminRequest request,
    User user
  ) {
    log.info("Attempting to update resume with id: {}", resumeId);

    try {
      Long id;
      try {
        id = Long.parseLong(resumeId);
      } catch (NumberFormatException e) {
        log.error("Invalid resume ID format: {}", resumeId);
        throw new IllegalArgumentException("Invalid resume ID format");
      }
      // String avatar =
      //   this.uploadServiceImplement.handleSaveUpLoadFile(file, "avatar");

      String avatar = "";

      System.err.println("Hello 1");
      Resume existingResume = resumeRepository
        .findById(resumeId)
        .orElseThrow(() ->
          new ResumeNotFoundException("Resume not found with id: " + resumeId)
        );

      existingResume.setTitle(request.getTitle());
      existingResume.setColorHex(request.getColorHex());
      existingResume.setBorderStyle(request.getBorderStyle());
      existingResume.setDescription(request.getDescription());
      existingResume.setPhotoUrl(avatar);
      existingResume.setSummary(request.getSummary());
      existingResume.setFirstName(request.getFirstName());
      existingResume.setLastName(request.getLastName());
      existingResume.setJobTitle(request.getJobTitle());
      existingResume.setCity(request.getCity());
      existingResume.setCountry(request.getCountry());
      existingResume.setPhone(request.getPhone());
      existingResume.setEmail(request.getEmail());

      if (request.getSkills() != null) {
        existingResume.setSkills(new ArrayList<>(request.getSkills()));
      } else {
        existingResume.getSkills().clear();
      }
      existingResume.setUpdatedAt(LocalDateTime.now());

      existingResume.getWorkExperiences().clear();

      if (request.getWorkExperiences() != null) {
        List<WorkExperience> newWorkExperiences = new ArrayList<>();
        for (WorkExperienceDTO dto : request.getWorkExperiences()) {
          WorkExperience workExperience = WorkExperience
            .builder()
            .position(dto.getPosition())
            .company(dto.getCompany())
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .description(dto.getDescription())
            .resume(existingResume)
            .build();
          newWorkExperiences.add(workExperience);
        }
        existingResume.getWorkExperiences().addAll(newWorkExperiences);
      }

      existingResume.getEducations().clear();

      if (request.getEducations() != null) {
        List<Education> newEducations = new ArrayList<>();
        for (EducationDTO dto : request.getEducations()) {
          Education education = Education
            .builder()
            .degree(dto.getDegree())
            .school(dto.getSchool())
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .resume(existingResume)
            .build();
          newEducations.add(education);
        }
        existingResume.getEducations().addAll(newEducations);
      }

      Resume updatedResume = resumeRepository.save(existingResume);

      log.info(
        "Successfully updated resume with id: {}",
        updatedResume.getId()
      );
      return updatedResume;
    } catch (Exception e) {
      System.err.println("Error" + e.getMessage());
      return null; // Added return statement
    }
  }

  @Transactional
  @Override
  public void findIdResumeToUpdatePhotoUrl(Long idResume, String photoUrl) {
    try {
      resumeRepository.updatePhotoUrlByResumeId(idResume, photoUrl);
    } catch (Exception e) {
      System.err.println("Messege Error -" + e.getMessage());
    }
  }

  @Transactional
  @Override
  public void findIdResumeToUpdatePhotoUrlToNull(Long idResume) {
    try {
      resumeRepository.updatePhotoUrlByResumeIdToNull(idResume);
    } catch (Exception e) {
      // TODO: handle exception
      System.err.println("Messege Error -" + e.getMessage());
    }
  }

  @Override
  public Resume EditResume(
    String resumeId,
    EditResumeAdminRequest request,
    User user
  ) {
    try {
      Long id;
      try {
        id = Long.parseLong(resumeId);
      } catch (NumberFormatException e) {
        log.error("Invalid resume ID format: {}", resumeId);
        throw new IllegalArgumentException("Invalid resume ID format");
      }
      // String avatar =
      //   this.uploadServiceImplement.handleSaveUpLoadFile(file, "avatar");

      String avatar = "";

      System.err.println("Hello 1");
      Resume existingResume = resumeRepository
        .findById(resumeId)
        .orElseThrow(() ->
          new ResumeNotFoundException("Resume not found with id: " + resumeId)
        );

      existingResume.setTitle(request.getTitle());
      existingResume.setColorHex(request.getColorHex());
      existingResume.setBorderStyle(request.getBorderStyle());
      existingResume.setDescription(request.getDescription());
      existingResume.setPhotoUrl(avatar);
      existingResume.setSummary(request.getSummary());
      existingResume.setFirstName(request.getFirstName());
      existingResume.setLastName(request.getLastName());
      existingResume.setJobTitle(request.getJobTitle());
      existingResume.setCity(request.getCity());
      existingResume.setCountry(request.getCountry());
      existingResume.setPhone(request.getPhone());
      existingResume.setEmail(request.getEmail());

      if (request.getSkills() != null) {
        existingResume.setSkills(new ArrayList<>(request.getSkills()));
      } else {
        existingResume.getSkills().clear();
      }
      existingResume.setUpdatedAt(LocalDateTime.now());

      existingResume.getWorkExperiences().clear();

      if (request.getWorkExperiences() != null) {
        List<WorkExperience> newWorkExperiences = new ArrayList<>();
        for (workExperienceResumeEditRequestDTO dto : request.getWorkExperiences()) {
          WorkExperience workExperience = WorkExperience
            .builder()
            .position(dto.getPosition())
            .company(dto.getCompany())
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .description(dto.getDescription())
            .resume(existingResume)
            .build();
          newWorkExperiences.add(workExperience);
        }
        existingResume.getWorkExperiences().addAll(newWorkExperiences);
      }

      existingResume.getEducations().clear();

      if (request.getEducations() != null) {
        List<Education> newEducations = new ArrayList<>();
        for (EducationResumeEdit dto : request.getEducations()) {
          Education education = Education
            .builder()
            .degree(dto.getDegree())
            .school(dto.getSchool())
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .resume(existingResume)
            .build();
          newEducations.add(education);
        }
        existingResume.getEducations().addAll(newEducations);
      }

      Resume updatedResume = resumeRepository.save(existingResume);

      log.info(
        "Successfully updated resume with id: {}",
        updatedResume.getId()
      );
      return updatedResume;
    } catch (Exception e) {
      System.err.println("Error" + e.getMessage());
      return null; // Added return statement
    }
  }
}
