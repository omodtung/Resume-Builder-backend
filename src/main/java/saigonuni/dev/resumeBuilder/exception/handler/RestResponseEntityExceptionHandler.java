package saigonuni.dev.resumeBuilder.exception.handler;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import saigonuni.dev.resumeBuilder.exception.BadRequestException;
import saigonuni.dev.resumeBuilder.exception.DuplicateKeyException;
import saigonuni.dev.resumeBuilder.exception.ResumeNotFoundException;
import saigonuni.dev.resumeBuilder.exception.UserNotFoundException;
import saigonuni.dev.resumeBuilder.message.UserMessage;
import saigonuni.dev.resumeBuilder.message.UserSubcription;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler
  extends ResponseEntityExceptionHandler {

  private static final String ERROR_CODE_INTERNAL = "INTERNAL_ERROR";
  private static final String BAD_REQUEST = "BAD_REQUEST";
  private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_TO_HTTP_STATUS_CODE = Map.of(
    ResumeNotFoundException.class,
    HttpStatus.NOT_FOUND,
    UserNotFoundException.class,
    HttpStatus.NOT_FOUND,
    DuplicateKeyException.class,
    HttpStatus.CONFLICT
  );

  private static final Map<Class<? extends RuntimeException>, String> EXCEPTION_TO_ERROR_CODE = Map.of(
    ResumeNotFoundException.class,
    "RESUME_NOT_FOUND",
    UserNotFoundException.class,
    "USER_NOT_FOUND",
    DuplicateKeyException.class,
    "DUPLICATE_KEY"
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

  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<ApiExceptionResponse> handleDuplicateKeyException(
    DuplicateKeyException exception
  ) {
    ApiExceptionResponse response = ApiExceptionResponse
      .builder()
      .status(HttpStatus.CONFLICT)
      .errorCode("DUPLICATE_KEY")
      .message(exception.getMessage())
      .build();

    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiExceptionResponse> handleBadRequestExceptionKeyException(
    BadRequestException exception
  ) {
    // HttpStatus httpStatus = EXCEPTION_TO_HTTP_STATUS_CODE.getOrDefault(
    //   exception.getClass(),
    //   HttpStatus.NOT_FOUND
    // );
    // String errorCode = EXCEPTION_TO_ERROR_CODE.getOrDefault(
    //   exception.getClass(),
    //   UserMessage.USER_NOT_FOUND_KEY
    // );

    ApiExceptionResponse response = ApiExceptionResponse
      .builder()
      .status(HttpStatus.BAD_REQUEST)
      .errorCode(exception.getKey())
      .message(exception.getMessage())
      .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
