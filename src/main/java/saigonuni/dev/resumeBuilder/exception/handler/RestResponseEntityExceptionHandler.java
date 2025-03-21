package saigonuni.dev.resumeBuilder.exception.handler;

import java.util.Map;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import saigonuni.dev.resumeBuilder.exception.ResumeNotFoundException;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler
  extends ResponseEntityExceptionHandler {

  private static final String ERROR_CODE_INTERNAL = "INTERNAL_ERROR";
  private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_TO_HTTP_STATUS_CODE = Map.of(
    ResumeNotFoundException.class,
    HttpStatus.NOT_FOUND
  );

  private static final Map<Class<? extends RuntimeException>, String> EXCEPTION_TO_ERROR_CODE = Map.of(
    ResumeNotFoundException.class,
    "RESUME_NOT_FOUND"
  );

  @ExceptionHandler
  ResponseEntity<ApiExceptionResponse> handleUserNotFoundException(
    RuntimeException exception
  ) {
    HttpStatus httpStatus = EXCEPTION_TO_HTTP_STATUS_CODE.getOrDefault(
      exception.getClass(),
      HttpStatus.INTERNAL_SERVER_ERROR
    );
    String errorCode = EXCEPTION_TO_ERROR_CODE.getOrDefault(
      exception.getClass(),
      ERROR_CODE_INTERNAL
    );

    final ApiExceptionResponse response = ApiExceptionResponse
      .builder()
      .status(httpStatus)
      .errorCode(errorCode)
      .build();

    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
