package saigonuni.dev.resumeBuilder.controller.admin;

import com.stripe.model.Account;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.common.enums.Company;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.resume.GetResumeAdminResponse;
import saigonuni.dev.resumeBuilder.repository.UserRepository;
import saigonuni.dev.resumeBuilder.service.ResumeService;
import saigonuni.dev.resumeBuilder.service.UploadService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UploadFileController {

  private UploadService uploadService;
  private final ResumeService resumeService;
  private final RestTemplate restTemplate;
  private final UserRepository userRepository;

  @Autowired
  UploadFileController(
    UploadService uploadService,
    ResumeService resumeService,
    RestTemplate restTemplate,
    UserRepository userRepository
  ) {
    this.uploadService = uploadService;
    this.resumeService = resumeService;
    this.restTemplate = restTemplate;
    this.userRepository = userRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000/")
  @PostMapping(
    value = "upload-file",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @LogExecutionTime
  public String HandleUploadFile(@RequestPart("File") MultipartFile file) {
    String target = "logo";
    return this.uploadService.handleSaveUpLoadFile(file, target);
  }

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping(
    value = "upload-file-cv",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @LogExecutionTime
  public String HandleUploadFileCV(
    @RequestPart("File") MultipartFile file,
    @RequestHeader("idResume") Long idResume
  ) {
    String photoUrl = this.uploadService.handleSaveUpLoadFile(file, "avatar");
    this.resumeService.findIdResumeToUpdatePhotoUrl(idResume, photoUrl);
    return photoUrl;
    // return "Success";
  }

  @PostMapping(value = "delete-upload-file-cv")
  @LogExecutionTime
  public String HandleUploadFileCV(
    // @RequestPart("File") MultipartFile file,
    @RequestHeader("idResume") Long idResume
  ) {
    this.resumeService.findIdResumeToUpdatePhotoUrlToNull(idResume);
    return "Success";
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping(value = "link-jobCV-upload-file-cv")
  public ResponseEntity<String> sendCvToPortal(
    @RequestParam("type") String type,
    Principal principal
  ) {
    try {
      if (principal == null) {
        return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User not authenticated.");
      }
      User user = userRepository.findByUsername(principal.getName());
      System.out.println("User In Open File" + user);
      String pipelineUrl = "http://localhost:8000/api/v1/resumes/pipeline";
      String status = "PENDING";
      String companyId = "";
      String jobId = "";

      if (Company.valueOf(type) == Company.VIETTEL) {
        companyId = "681ce4d6780e16b3909f4dab";
        jobId = "681ce5b0ef2a4617f0a90c7c";
      }
      if (Company.valueOf(type) == Company.VNG) {
        companyId = "681ce4d6780e16b3909f4dab";
        jobId = "681ce5b0ef2a4617f0a90c7c";
      }
      if (Company.valueOf(type) == Company.FPT) {
        companyId = "681ce4d6780e16b3909f4dab";
        jobId = "681ce5b0ef2a4617f0a90c7c";
      }
      if (Company.valueOf(type) == Company.NAB) {
        companyId = "681ce4d6780e16b3909f4dab";
        jobId = "681ce5b0ef2a4617f0a90c7c";
      }

      MultiValueMap<String, Object> pipelineBody = new LinkedMultiValueMap<>();
      pipelineBody.add("email", user.getEmail());
      pipelineBody.add("status", status);
      pipelineBody.add("companyId", companyId);
      pipelineBody.add("jobId", jobId);

      HttpHeaders pipelineHeaders = new HttpHeaders();
      pipelineHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      HttpEntity<MultiValueMap<String, Object>> pipelineRequestEntity = new HttpEntity<>(
        pipelineBody,
        pipelineHeaders
      );

      ResponseEntity<String> pipelineResponse = restTemplate.postForEntity(
        pipelineUrl,
        pipelineRequestEntity,
        String.class
      );

      return pipelineResponse;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  @PostMapping(
    value = "file-open-send",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<String> SendFileToUploadOpenFileNest(
    @RequestParam("File") MultipartFile file
  ) {
    try {
      String uploadOpenUrl = "http://localhost:8000/api/v1/files/upload-Open";

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("fileUpload", file.getResource());

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
        body,
        headers
      );

      ResponseEntity<String> response = restTemplate.postForEntity(
        uploadOpenUrl,
        requestEntity,
        String.class
      );

      return response;
    } catch (Exception e) {
      // Handle any exceptions that occur during the request
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error: " + e.getMessage());
    }
  }

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping(
    value = "file-cv-send-ai",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<String> SendFileToAISpringBoot2(
    @RequestParam("File") MultipartFile file,
    Principal principal
  ) {
    try {
      User user = userRepository.findByUsername(principal.getName());
      String uploadOpenUrl = "http://localhost:8081/upload-file";
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("File", file.getResource());
      body.add("userId", user.getId());
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
        body,
        headers
      );
      ResponseEntity<String> response = restTemplate.postForEntity(
        uploadOpenUrl,
        requestEntity,
        String.class
      );
      return response;
    } catch (Exception e) {
      // Handle any exceptions that occur during the request
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error: " + e.getMessage());
    }
  }
}
