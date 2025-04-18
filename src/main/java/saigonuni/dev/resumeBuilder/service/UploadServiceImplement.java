package saigonuni.dev.resumeBuilder.service;

import jakarta.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImplement implements UploadService {

  private final ServletContext servletContext;

  public UploadServiceImplement(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  public String handleSaveUpLoadFile(MultipartFile file, String targetFoler) {
    System.err.println("Test 1");
    String rootPath = "/home/dothetung/Projects/Spring-Boot/resumeBuilder/resumeBuilder/src/main/resources/images";
    System.out.println("Root path: " + rootPath); // Log the root path
    String finalName = "";
    try {
      System.err.println("Test 2");
      byte[] bytes = file.getBytes();
      File dir = new File(rootPath + File.separator + targetFoler);
      if (!dir.exists()) dir.mkdirs();
      String originalFilename = file.getOriginalFilename();
      if (originalFilename == null) {
        // Handle null filename
        originalFilename = "default-filename.jpg"; // Provide a default name
      }
      finalName = System.currentTimeMillis() + "-" + originalFilename;
      File serverFile = new File(
        dir.getAbsolutePath() + File.separator + finalName
      );

      try (BufferedOutputStream stream = new BufferedOutputStream(
        new FileOutputStream(serverFile)
      )) {
        stream.write(bytes);
      }
    } catch (IOException e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    return finalName;
  }
}