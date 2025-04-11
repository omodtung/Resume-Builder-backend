package saigonuni.dev.resumeBuilder.exception;

public class BadRequestException extends RuntimeException {

  private String key;
  private String message;

  public BadRequestException(String message, String key) {
    super(message);
    this.message = message;
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
