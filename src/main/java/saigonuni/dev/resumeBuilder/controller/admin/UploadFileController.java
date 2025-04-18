package saigonuni.dev.resumeBuilder.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.dto.resume.GetResumeAdminResponse;
import saigonuni.dev.resumeBuilder.service.UploadService;

@RestController
public class UploadFileController {

  private UploadService uploadService;

  @Autowired
  UploadFileController(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  @PostMapping("upload-file")
  @LogExecutionTime
  public void HandleUploadFile(
    @RequestPart(value = "File", required = false) MultipartFile file
  ) {
    String targetFoler = "photoUrl";
    this.uploadService.handleSaveUpLoadFile(file, targetFoler);
  }
}
