package saigonuni.dev.resumeBuilder.service;

import java.util.List;


import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.ResumeResponseDTO;

@Service
public interface ResumeService {
  Resume addResume(CreateResumeAdminRequest request ,User user);
  Resume getResumeById(String id);
  List<Resume> listResumes();
  void deleteResume(String id);
  Resume updateResume(String resumeId, CreateResumeAdminRequest request,User user );
}
