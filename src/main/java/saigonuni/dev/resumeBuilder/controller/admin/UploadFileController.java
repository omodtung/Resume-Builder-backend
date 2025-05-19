package saigonuni.dev.resumeBuilder.controller.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.services.blocking.fineTuning.JobService;
import com.stripe.model.Account;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
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
import saigonuni.dev.resumeBuilder.domain.Jobs;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.OpenAi.ObjectModelAI;
import saigonuni.dev.resumeBuilder.dto.OpenAi.QueryRequest;
import saigonuni.dev.resumeBuilder.dto.resume.GetResumeAdminResponse;
import saigonuni.dev.resumeBuilder.repository.UserRepository;
import saigonuni.dev.resumeBuilder.service.JobsService;
import saigonuni.dev.resumeBuilder.service.ResumeService;
import saigonuni.dev.resumeBuilder.service.UploadService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UploadFileController {

  private UploadService uploadService;
  private final ResumeService resumeService;
  private final RestTemplate restTemplate;
  private final UserRepository userRepository;
  private final JobsService jobService;

  @Autowired // Đảm bảo bạn có dòng này để inject ObjectMapper
  private ObjectMapper objectMapper;

  @Autowired
  UploadFileController(
    UploadService uploadService,
    ResumeService resumeService,
    RestTemplate restTemplate,
    UserRepository userRepository,
    JobsService jobService
  ) {
    this.uploadService = uploadService;
    this.resumeService = resumeService;
    this.restTemplate = restTemplate;
    this.userRepository = userRepository;
    this.jobService = jobService;
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

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
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
      Jobs job = new Jobs();
      job.setCompany(companyId);
      job.setEmail(user.getEmail());
      job.setJobId(jobId);
      jobService.addJobs(job);
      return pipelineResponse;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping(
    value = "file-open-send",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<String> SendFileToUploadOpenFileNest(
    @RequestParam("File") MultipartFile file
  ) {
    try {
      String uploadOpenUrl = "http://localhost:8000/api/v1/files/upload-Open";
      Jobs job = new Jobs();
      job.setCompany(uploadOpenUrl);
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

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping(
    value = "file-cv-match-ai",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<String> SendFileToModelAI(
    @RequestParam("File") MultipartFile file,
    Principal principal
  ) {
    try {
      User user = userRepository.findByUsername(principal.getName());
      String uploadOpenUrl = "http://localhost:8081/upload-file-model-ai";
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

  // @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  // @PostMapping(value = "send-skill-to-model-ai")
  public QueryRequest sendSkillToModelAi(
    //  ObjectModelAI input
    String skills,
    String userId
  ) {
    try {
      String pipelineUrl = "http://192.168.1.69:5000/predict";

    
      Map<String, String> requestBodyMap = new HashMap<>();
      requestBodyMap.put("skills_text", skills);
      HttpHeaders pipelineHeaders = new HttpHeaders();
      pipelineHeaders.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<Map<String, String>> pipelineRequestEntity = new HttpEntity<>(
        requestBodyMap,
        pipelineHeaders
      );
      ResponseEntity<String> pipelineResponse = restTemplate.postForEntity(
        pipelineUrl, // URL của API /predict
        pipelineRequestEntity, // HttpEntity chứa JSON body và JSON headers
        String.class // Vẫn mong muốn nhận response từ /predict dưới dạng String (JSON thô)
      );

      // if (pipelineResponse.getStatusCode().is2xxSuccessful() && pipelineResponse.hasBody()) {
      String jsonResponseFromPredict = pipelineResponse.getBody();
      String predictedCategoryId = null;

      try {
        // Parse JSON thô để lấy predicted_category_id
        JsonNode rootNode = objectMapper.readTree(jsonResponseFromPredict);
        JsonNode categoryNode = rootNode.path("predicted_category_id");
        if (!categoryNode.isMissingNode() && categoryNode.isTextual()) {
          predictedCategoryId = categoryNode.asText();
        } else {
          System.err.println(
            "Field 'predicted_category_id' not found or not a string in /predict response: " +
            jsonResponseFromPredict
          );
          // Quyết định trả về lỗi hay một QueryRequest với query là thông báo lỗi
          QueryRequest errorQueryRequest = new QueryRequest();
          errorQueryRequest.setUserId(userId);
          errorQueryRequest.setQuery(
            "Error: Could not extract predicted_category_id from prediction service."
          );
          // return ResponseEntity
          //   .status(HttpStatus.INTERNAL_SERVER_ERROR)
          //   .body(errorQueryRequest);
        }
      } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
        System.err.println(
          "Error parsing JSON response from /predict: " + e.getMessage()
        );
        QueryRequest errorQueryRequest = new QueryRequest();
        errorQueryRequest.setUserId(userId);
        errorQueryRequest.setQuery(
          "Error: Could not parse response from prediction service."
        );
        // return ResponseEntity
        //   .status(HttpStatus.INTERNAL_SERVER_ERROR)
        //   .body(errorQueryRequest);
      }
      // }
      QueryRequest resultQueryRequest = new QueryRequest();
      resultQueryRequest.setUserId(userId);
      resultQueryRequest.setQuery(predictedCategoryId); // Đặt predicted_category_id vào trường 'query'

      // return ResponseEntity.ok(resultQueryRequest);
      return resultQueryRequest ;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }
}
