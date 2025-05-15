package saigonuni.dev.resumeBuilder.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.domain.Jobs;
import saigonuni.dev.resumeBuilder.domain.WorkExperience;
import saigonuni.dev.resumeBuilder.repository.WorkExperienceRepository;

@RestController
@RequestMapping("admin")
public class WorkExperienceController {

  private final WorkExperienceRepository workExperienceRepo;

  public WorkExperienceController(WorkExperienceRepository workExperienceRepo) {
    this.workExperienceRepo = workExperienceRepo;
  }

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  @GetMapping("workExperience-filter")
  public ResponseEntity<Map<String, Object>> getWorkExperienceFilter(
    @RequestParam(required = false) String sort,
    @RequestParam(required = false) String filter,
    @RequestParam(defaultValue = "desc") String order,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int limit
  ) {
    try {
      Sort.Direction direction = "asc".equalsIgnoreCase(order)
        ? Sort.Direction.ASC
        : Sort.Direction.DESC;
      Pageable paging = PageRequest.of(page, limit, Sort.by(direction, "id"));
      Page<WorkExperience> pageJobs;

      if (filter != null && !filter.trim().isEmpty()) {
        pageJobs =
        workExperienceRepo.searchByTermAcrossFieldsWithColumm(
            filter.trim(),
            sort,
            paging
          );
      } else {
        pageJobs = workExperienceRepo.findAll(paging);
      }

      List<WorkExperience> jobs = pageJobs.getContent();

      Map<String, Object> response = new HashMap<>();
      response.put("data", jobs);
      response.put("currentPage", pageJobs.getNumber());
      response.put("totalItems", pageJobs.getTotalElements());
      response.put("totalPages", pageJobs.getTotalPages());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("message", "Error retrieving plans: " + e.getMessage());
      return new ResponseEntity<>(
        errorResponse,
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }
}
