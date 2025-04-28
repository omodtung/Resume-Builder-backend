package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.common.Decorations.Decode;
import saigonuni.dev.resumeBuilder.common.Decorations.Decode;
import saigonuni.dev.resumeBuilder.common.Decorations.UserDC;
import saigonuni.dev.resumeBuilder.common.validate.CheckSubcriptionWithUserId;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.domain.UserSubscription;
import saigonuni.dev.resumeBuilder.dto.UserSubscription.UserSupcriptionDTO;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.DeleteResumeResponse;
import saigonuni.dev.resumeBuilder.dto.resume.EditResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.GetResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminResponse;
import saigonuni.dev.resumeBuilder.repository.ResumeRepository;
import saigonuni.dev.resumeBuilder.service.JwtService;
import saigonuni.dev.resumeBuilder.service.OpenAiResumeService;
import saigonuni.dev.resumeBuilder.service.ResumeService;
import saigonuni.dev.resumeBuilder.service.UserSubcriptionService;

@Tag(
  name = "Resume Admin Controller",
  description = "Operations pertaining to admin management of resumes"
)
@RestController
@RequestMapping("admin")
public class ResumeAdminController extends BaseController {

  private JwtService jwtService;
  private final ResumeService resumeService;
  private final UserDC userDC;
  private final Decode decode;
  private final ResumeRepository resumeRepository;
  private final UserSubcriptionService userSubcriptionService;
  private final CheckSubcriptionWithUserId checkSubcriptionWithUserId;
  private final OpenAiResumeService OpenService;

  @Autowired
  public ResumeAdminController(
    ResumeService resumeService,
    JwtService jwtService,
    Decode decode,
    UserDC userDC,
    ResumeRepository resumeRepository,
    UserSubcriptionService userSubcriptionService,
    CheckSubcriptionWithUserId checkSubcriptionWithUserId,
    OpenAiResumeService OpenService
  ) {
    this.resumeService = resumeService;
    this.jwtService = jwtService;
    this.decode = decode;
    this.userDC = userDC;
    this.resumeRepository = resumeRepository;
    this.userSubcriptionService = userSubcriptionService;
    this.checkSubcriptionWithUserId = checkSubcriptionWithUserId;
    this.OpenService = OpenService;
  }

  // @PostMapping("resumes")
 @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping(value = "resumes")
  @Operation(
    summary = "API Thêm Resume mới",
    description = "Returns a list of all resumes"
  )
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<CreateResumeAdminResponse> addResume(
    @Valid @RequestBody(required = false) CreateResumeAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    try {
      User user = userDC.findUserNameByToken(
        decode.AuthenticationDecode(authorizationHeader)
      );

      if (request == null) {
        request = CreateResumeAdminRequest.emptyResume();
      }

      checkSubcriptionWithUserId.permissionForEachPlan(user.getId());
      Resume resume = resumeService.addResume(request, user, null);
      return ResponseEntity.ok(
        CreateResumeAdminResponse.builder().resume(resume).build()
      );
    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new RuntimeException("Error processing request: " + e.getMessage());
    }
  }
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @GetMapping("resumes/{id}")
  @LogExecutionTime
  public ResponseEntity<GetResumeAdminResponse> getResumeById(
    @PathVariable String id
  ) {
    Resume resume = resumeService.getResumeById(id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(GetResumeAdminResponse.builder().resume(resume).build());
  }

  // @GetMapping("resumes")
  // @LogExecutionTime
  // public ResponseEntity<ListResumeResponse> getResume() {
  //   List<Resume> resumes = resumeService.listResumes();
  //   return ResponseEntity
  //     .status(HttpStatus.OK)
  //     .body(ListResumeResponse.builder().resume(resumes).build());
  // }
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @GetMapping("resumes")
  public ResponseEntity<Map<String, Object>> getResumes(
    @RequestParam(required = false) String sort,
    @RequestParam(required = false) String order,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int limit
  ) {
    // List<Resume> resumes = resumeService.listResumes();
    // return ResponseEntity.ok(resumes);
    try {
      List<Resume> resume; // Initialize later
      Sort.Direction direction = "asc".equalsIgnoreCase(order)
        ? Sort.Direction.ASC
        : Sort.Direction.DESC;
      Pageable paging;

      if (sort != null && !sort.isEmpty()) {
        paging = PageRequest.of(page, limit, Sort.by(direction, sort));
      } else {
        paging = PageRequest.of(page, limit); // Default paging without sort
      }

      Page<Resume> pageTuts = resumeService.findAll(paging);
      if (sort == null) pageTuts =
        resumeService.findAll(paging); else pageTuts =
        resumeService.findByTitleContaining(sort, paging);
      resume = pageTuts.getContent();

      List<Map<String, Object>> resumes = new ArrayList<>();
      for (Resume resumex : pageTuts.getContent()) {
        Map<String, Object> resumeMap = new HashMap<>();
        resumeMap.put("id", resumex.getId());
        resumeMap.put("title", resumex.getTitle());
        resumeMap.put("description", resumex.getDescription());
        resumeMap.put("photoUrl", resumex.getPhotoUrl());
        resumeMap.put("colorHex", resumex.getColorHex());
        resumeMap.put("borderStyle", resumex.getBorderStyle());
        resumeMap.put("summary", resumex.getSummary());
        resumeMap.put("firstName", resumex.getFirstName());
        resumeMap.put("lastName", resumex.getLastName());
        resumeMap.put("jobTitle", resumex.getJobTitle());
        resumeMap.put("city", resumex.getCity());
        resumeMap.put("country", resumex.getCountry());
        resumeMap.put("phone", resumex.getPhone());
        resumeMap.put("email", resumex.getEmail());
        resumeMap.put("userValue", resumex.getUserValue().getId());
        resumeMap.put("workExperiences", resumex.getWorkExperiences());
        resumeMap.put("educations", resumex.getEducations());
        resumeMap.put("skills", resumex.getSkills());
        resumeMap.put("type", resumex.getType());
        resumes.add(resumeMap);
      }

      Map<String, Object> response = new HashMap<>();
      response.put("data", resumes);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @GetMapping("resumes-filter")
  public ResponseEntity<Map<String, Object>> getResumesFilter(
    @RequestParam(required = false) String sort,
    @RequestParam(required = false) String filter,
    @RequestParam(required = false) String order,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int limit
  ) {
    // List<Resume> resumes = resumeService.listResumes();
    // return ResponseEntity.ok(resumes);
    try {
      List<Resume> resume; // Initialize later
      Sort.Direction direction = "asc".equalsIgnoreCase(order)
        ? Sort.Direction.ASC
        : Sort.Direction.DESC;
      Pageable paging;

      if (sort != null && !sort.isEmpty()) {
        paging = PageRequest.of(page, limit, Sort.by(direction, sort));
      } else {
        paging = PageRequest.of(page, limit); // Default paging without sort
      }

      Page<Resume> pageTuts = resumeService.findAll(paging);
      if (sort == null) pageTuts =
        resumeService.findAll(paging); else pageTuts =
        resumeRepository.searchByTermAcrossFieldsWithColumn(
          filter,
          sort,
          paging
        );
      // resumeService.findByTitleContaining(sort, paging);

      resume = pageTuts.getContent();

      List<Map<String, Object>> resumes = new ArrayList<>();
      for (Resume resumex : pageTuts.getContent()) {
        Map<String, Object> resumeMap = new HashMap<>();
        resumeMap.put("id", resumex.getId());
        resumeMap.put("title", resumex.getTitle());
        resumeMap.put("description", resumex.getDescription());
        resumeMap.put("photoUrl", resumex.getPhotoUrl());
        resumeMap.put("colorHex", resumex.getColorHex());
        resumeMap.put("borderStyle", resumex.getBorderStyle());
        resumeMap.put("summary", resumex.getSummary());
        resumeMap.put("firstName", resumex.getFirstName());
        resumeMap.put("lastName", resumex.getLastName());
        resumeMap.put("jobTitle", resumex.getJobTitle());
        resumeMap.put("city", resumex.getCity());
        resumeMap.put("country", resumex.getCountry());
        resumeMap.put("phone", resumex.getPhone());
        resumeMap.put("email", resumex.getEmail());
        resumeMap.put("userValue", resumex.getUserValue().getId());
        resumeMap.put("workExperiences", resumex.getWorkExperiences());
        resumeMap.put("educations", resumex.getEducations());
        resumeMap.put("skills", resumex.getSkills());
        resumeMap.put("type", resumex.getType());
        resumes.add(resumeMap);
      }

      Map<String, Object> response = new HashMap<>();
      response.put("data", resumes);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PatchMapping("resumes/{id}")
  @Operation(
    summary = "API Update Resume Support For Auto Save ",
    description = "Update API Resume For Auto Save"
  )
  @LogExecutionTime
  public ResponseEntity<UpdateResumeAdminResponse> updateResume(
    @PathVariable String id,
    @Valid @RequestBody UpdateResumeAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
    // Removed MultipartFile parameter
  ) {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );
    if (request == null) {
      request = UpdateResumeAdminRequest.emptyResume();
    }
    Resume resume = resumeService.updateResume(id, request, user);

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(UpdateResumeAdminResponse.builder().resume(resume).build());
  }
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PatchMapping("resumes-edit/{id}")
  @Operation(
    summary = "API Update Resume  ",
    description = "Update API Resume "
  )
  @LogExecutionTime
  public ResponseEntity<UpdateResumeAdminResponse> EditResume(
    @PathVariable String id,
    @Valid @RequestBody EditResumeAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
    // Removed MultipartFile parameter
  ) {
    User user = userDC.findUserNameByToken(
      decode.AuthenticationDecode(authorizationHeader)
    );

    Resume resume = resumeService.EditResume(id, request, user);

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(UpdateResumeAdminResponse.builder().resume(resume).build());
  }
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @DeleteMapping("resumes/{id}")
  public ResponseEntity<DeleteResumeResponse> deleteResume(
    @PathVariable String id
  ) {
    resumeService.deleteResume(id);
    return ResponseEntity.ok().build();
  }
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @PostMapping("api/openai/summary")
  public String generateSummary() {
    try {
      System.out.println("Input Testing : ");
      return "Say";
    } catch (RuntimeException e) {
      // Basic error handling
      e.printStackTrace(); // Log the full stack trace
    }
    return null;
  }
}
