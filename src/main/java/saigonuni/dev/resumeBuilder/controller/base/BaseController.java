package saigonuni.dev.resumeBuilder.controller.base;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import java.net.http.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

  @Autowired
  protected HttpServletRequest request;
}
