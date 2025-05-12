package saigonuni.dev.resumeBuilder.dto.OpenAi;

public class ChatResponse {
    private String response;
    private String responseId; // Corresponds to conversationId

    // Default constructor
    public ChatResponse() {}

    // Getters and Setters
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public String getResponseId() { return responseId; }
    public void setResponseId(String responseId) { this.responseId = responseId; }
}
