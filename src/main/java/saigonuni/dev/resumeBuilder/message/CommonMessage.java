package saigonuni.dev.resumeBuilder.message;

public enum CommonMessage {
  DUPLICATE_KEY("Username or email already exists"),
  USER_NOT_FOUND("User not found"),
  INVALID_REQUEST("Invalid request");

  private final String message;

  CommonMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
