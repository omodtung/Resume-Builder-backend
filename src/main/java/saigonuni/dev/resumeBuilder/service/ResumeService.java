package saigonuni.dev.resumeBuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.repository.ResumeRepository;

@Service
public class ResumeService {

  private final ResumeRepository resumeRepository;

  @Autowired
  public ResumeService(ResumeRepository resumeRepository) {
    this.resumeRepository = resumeRepository;
  }

  public Resume handleSaveResume(Resume resume) {
    return resumeRepository.save(resume);
  }
}
