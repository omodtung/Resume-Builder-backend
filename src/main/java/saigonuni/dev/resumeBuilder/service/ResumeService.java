package saigonuni.dev.resumeBuilder.service;

import java.util.List;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.ListResumesRequest;

@Service
public interface ResumeService {
  Resume addResume(CreateResumeAdminRequest request);
  Resume getResumeById(String id);
  List<Resume> listResumes();
}
