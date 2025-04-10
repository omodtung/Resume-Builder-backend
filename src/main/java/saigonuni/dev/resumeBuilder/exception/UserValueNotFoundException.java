package saigonuni.dev.resumeBuilder.exception;

public class UserValueNotFoundException extends RuntimeException {
  public UserValueNotFoundException() {
    super("User value not found.");
  }
}
