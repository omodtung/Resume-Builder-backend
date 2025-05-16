package saigonuni.dev.resumeBuilder.config;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional; // Q
import org.springframework.web.client.RestTemplate;
import saigonuni.dev.resumeBuilder.domain.Data;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.repository.DataRepository;
import saigonuni.dev.resumeBuilder.repository.ResumeRepository;
import saigonuni.dev.resumeBuilder.service.ResumeService;

@Configuration
@EnableAsync
@EnableScheduling
public class SpringConfig {

  private final ResumeRepository resumeRepositoty;
  private final ResumeService resumeService;
  private final DataRepository dataRepository;

  public SpringConfig(
    ResumeRepository resumeRepositoty,
    ResumeService resumeService,
    DataRepository dataRepository
  ) {
    this.resumeRepositoty = resumeRepositoty;
    this.resumeService = resumeService;
    this.dataRepository = dataRepository;
  }

  private String extractCompanyNameFromTitle(String title) {
    if (title == null || title.isEmpty()) {
      return null;
    }
    // Ví dụ đơn giản: "Job Title At Company Name"
    int atIndex = title.toLowerCase().lastIndexOf(" at ");
    if (atIndex != -1 && atIndex + 4 < title.length()) {
      return title.substring(atIndex + 4).trim();
    }

    return null; // Hoặc một giá trị mặc định nếu không trích xuất được
  }

  // @Scheduled(fixedDelay = 10000)
  @Transactional
  public void rawDataResume() {
    List<Resume> resumes = resumeService.listResumes();
    if (resumes.isEmpty()) {
      
      return;
    }

    int newRecordsSaved = 0;
    int skippedRecords = 0;

    for (Resume resume : resumes) {
      if (resume.getId() == null) {
        //log.warn("Resume found with null ID. Skipping this resume.");
        continue;
      }
      String resumeIdStr = String.valueOf(resume.getId());

      // Kiểm tra xem resume này đã được xử lý và lưu trữ trước đó chưa
      if (dataRepository.existsByIdResume(resumeIdStr)) {
        //log.debug(
        //   "Resume with ID {} already processed and stored. Skipping.",
        //   resumeIdStr
        // );
        skippedRecords++;
        continue;
      }

      // Chuẩn bị chuỗi skills
      String skillsString = ""; // Mặc định là chuỗi rỗng
      if (resume.getSkills() != null && !resume.getSkills().isEmpty()) {
        skillsString =
          resume
            .getSkills()
            .stream()
            .filter(s -> s != null && !s.trim().isEmpty()) // Lọc skill null hoặc rỗng
            .map(String::trim) // Loại bỏ khoảng trắng thừa
            .collect(Collectors.joining(",")); // Nối bằng dấu phẩy
      }

      // Lấy thông tin company từ trường 'type' của Resume
      String companySourceType = resume.getType();
      if (companySourceType == null || companySourceType.trim().isEmpty()) {
        //log.warn(
        //   "Resume ID {} has null or empty 'type'. Using default 'N/A' for company field.",
        //   resumeIdStr
        // );
        companySourceType = "N/A"; // Giá trị mặc định nếu type là null hoặc rỗng
      }

      // Tạo đối tượng Data để lưu
      Data dataToSave = Data
        .builder()
        .idResume(resumeIdStr)
        .skill(skillsString)
        .company(companySourceType.trim())
        .build();

      try {
        dataRepository.save(dataToSave);
        newRecordsSaved++;
        //log.info(
          // "Saved data for resume ID: {}. Skills: [{}], Company Type: [{}]",
          // resumeIdStr,
          // skillsString,
          // companySourceType.trim()
        // );
      } catch (Exception e) {
        // Lỗi này có thể xảy ra nếu có race condition và unique constraint trên idResume bị vi phạm,
        // mặc dù đã có kiểm tra existsByIdResume.
        //log.error(
          // "Error saving data for resume ID {}: {}",
          // resumeIdStr,
          // e.getMessage()
          // e
        // );
        // Tùy thuộc vào yêu cầu, bạn có thể muốn dừng job hoặc chỉ //log và tiếp tục
      }
    }
    //log.info(
    //   "Scheduled task finished. New records saved: {}. Records skipped (already exist): {}.",
    //   newRecordsSaved,
    //   skippedRecords
    // );
  }
}
