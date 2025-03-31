package saigonuni.dev.resumeBuilder.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;

public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Map<String, String>> handleBadRequestException(
    BadRequestException ex
  ) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("key", ex.getKey());
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.badRequest().body(errorResponse);
  }
}
