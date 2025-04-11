package saigonuni.dev.resumeBuilder.exception;

public class ResumeNotFoundException extends RuntimeException {
    public ResumeNotFoundException() {
    }

    public ResumeNotFoundException(String message) {
        super(message);
    }
}
