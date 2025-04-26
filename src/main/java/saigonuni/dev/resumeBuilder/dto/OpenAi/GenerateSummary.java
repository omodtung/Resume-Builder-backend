package saigonuni.dev.resumeBuilder.dto.OpenAi;

import java.util.List;

public class GenerateSummary {

  public static class OpenAiMessage {

    private String role;
    private String content;

    public OpenAiMessage(String role, String content) {
      this.role = role;
      this.content = content;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }
  }

  public static class OpenAiChatCompletionRequest {

    private String model;
    private List<OpenAiMessage> messages;

    public OpenAiChatCompletionRequest(
      String model,
      List<OpenAiMessage> messages
    ) {
      this.model = model;
      this.messages = messages;
    }

    public String getModel() {
      return model;
    }

    public void setModel(String model) {
      this.model = model;
    }

    public List<OpenAiMessage> getMessages() {
      return messages;
    }

    public void setMessages(List<OpenAiMessage> messages) {
      this.messages = messages;
    }
  }

  public static class OpenAiChatCompletionResponse {

    private List<Choice> choices;

    public List<Choice> getChoices() {
      return choices;
    }

    public void setChoices(List<Choice> choices) {
      this.choices = choices;
    }

    public static class Choice {

      private Message message;

      public Message getMessage() {
        return message;
      }

      public void setMessage(Message message) {
        this.message = message;
      }

      public static class Message {

        private String content;

        public String getContent() {
          return content;
        }

        public void setContent(String content) {
          this.content = content;
        }
      }
    }
  }
}
