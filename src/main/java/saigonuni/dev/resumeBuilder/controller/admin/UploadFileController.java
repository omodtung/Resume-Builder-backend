package saigonuni.dev.resumeBuilder.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.dto.resume.GetResumeAdminResponse;
import saigonuni.dev.resumeBuilder.service.ResumeService;
import saigonuni.dev.resumeBuilder.service.UploadService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UploadFileController {

  private UploadService uploadService;
  private final ResumeService resumeService;

  @Autowired
  UploadFileController(
    UploadService uploadService,
    ResumeService resumeService
  ) {
    this.uploadService = uploadService;
    this.resumeService = resumeService;
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

  @CrossOrigin(origins = "http://localhost:3000/")
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
    return "Success";
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
}
