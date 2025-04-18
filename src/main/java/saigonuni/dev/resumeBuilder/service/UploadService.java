package saigonuni.dev.resumeBuilder.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadService {
  public String handleSaveUpLoadFile(MultipartFile file, String targetFolder);
}
