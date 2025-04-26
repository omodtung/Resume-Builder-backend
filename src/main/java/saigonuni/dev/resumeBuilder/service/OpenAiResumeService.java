package saigonuni.dev.resumeBuilder.service;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.OpenAi.GenerateSummary;
import saigonuni.dev.resumeBuilder.dto.OpenAi.GenerateSummary.*;
import saigonuni.dev.resumeBuilder.dto.OpenAi.SummaryCall;
import saigonuni.dev.resumeBuilder.dto.OpenAi.SummaryCall;

@Service
public class OpenAiResumeService {

  @Value("${OPENAI_API_KEY}")
  private String openaiApiKey;

  private final RestTemplate restTemplate = new RestTemplate();
  private final String openaiApiUrl =
    "https://api.openai.com/v1/chat/completions";

  public String generateSummary(@Valid SummaryCall input) {
    // TODO: Block for non-premium users
    System.out.println("Input Testing : " + input);

    try {
      String systemMessage =
        """
            You are a job resume generator AI. Your task is to write a professional introduction summary for a resume given the user's provided data.
            Only return the summary and do not include any other information in the response. Keep it concise and professional.
            """;

      StringBuilder userMessageBuilder = new StringBuilder();
      userMessageBuilder.append(
        "Please generate a professional resume summary from this data:\n\n"
      );
      userMessageBuilder
        .append("Job title: ")
        .append(input.getJobTitle() != null ? input.getJobTitle() : "N/A")
        .append("\n\n");

      userMessageBuilder
        .append("Skills:\n")
        .append(input.getSkills() != null ? input.getSkills() : "N/A");

      GenerateSummary generateSummary = new GenerateSummary();
      List<OpenAiMessage> messages = List.of(
        new OpenAiMessage("system", systemMessage),
        new OpenAiMessage("user", userMessageBuilder.toString())
      );
      OpenAiChatCompletionRequest request =
        new GenerateSummary.OpenAiChatCompletionRequest(
            "gpt-4o-mini",
            messages
          );

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(openaiApiKey);

      HttpEntity<OpenAiChatCompletionRequest> entity = new HttpEntity<>(
        request,
        headers
      );

      OpenAiChatCompletionResponse response = restTemplate.postForObject(
        openaiApiUrl,
        entity,
        OpenAiChatCompletionResponse.class
      );

      if (
        response == null ||
        response.getChoices() == null ||
        response.getChoices().isEmpty()
      ) {
        throw new RuntimeException("Failed to generate AI response");
      }

      return response.getChoices().get(0).getMessage().getContent();
    } catch (Exception e) {
      System.out.println("Error generating summary: " + e.getMessage());
      e.printStackTrace(); // Print the stack trace for detailed error information
      // Handle the error as needed
      throw new RuntimeException("Error generating summary: " + e.getMessage());
    }
  }

  // public WorkExperience generateWorkExperience(
  //   @Valid GenerateWorkExperienceInput input
  // ) {
  //   // TODO: Block for non-premium users

  //   String systemMessage =
  //     """
  //           You are a job resume generator AI. Your task is to generate a single work experience entry based on the user input.
  //           Your response must adhere to the following structure. You can omit fields if they can't be infered from the provided data, but don't add any new ones.

  //           Job title: <job title>
  //           Company: <company name>
  //           Start date: <format: YYYY-MM-DD> (only if provided)
  //           End date: <format: YYYY-MM-DD> (only if provided)
  //           Description: <an optimized description in bullet format, might be infered from the job title>
  //           """;

  //   String userMessage =
  //     "Please provide a work experience entry from this description:\n" +
  //     input.getDescription();

  //   List<OpenAiMessage> messages = List.of(
  //     new GenerateSummary().new OpenAiMessage("system", systemMessage),
  //     new GenerateSummary().new OpenAiMessage("user", userMessage)
  //   );

  //   GenerateSummary generateSummaryInstance = new GenerateSummary();
  //   OpenAiChatCompletionRequest request =
  //     generateSummaryInstance.new OpenAiChatCompletionRequest(
  //         "gpt-4o-mini",
  //         messages
  //       );

  //   HttpHeaders headers = new HttpHeaders();
  //   headers.setContentType(MediaType.APPLICATION_JSON);
  //   headers.setBearerAuth(openaiApiKey);

  //   HttpEntity<OpenAiChatCompletionRequest> entity = new HttpEntity<>(
  //     request,
  //     headers
  //   );

  //   OpenAiChatCompletionResponse response = restTemplate.postForObject(
  //     openaiApiUrl,
  //     entity,
  //     OpenAiChatCompletionResponse.class
  //   );

  //   if (
  //     response == null ||
  //     response.getChoices() == null ||
  //     response.getChoices().isEmpty()
  //   ) {
  //     throw new RuntimeException("Failed to generate AI response");
  //   }

  //   String aiResponse = response.getChoices().get(0).getMessage().getContent();

  //   WorkExperience workExperience =
  //     generateSummaryInstance.new WorkExperience();
  //   // Basic parsing of the AI response - more robust parsing might be needed
  //   for (String line : aiResponse.split("\n")) {
  //     if (line.startsWith("Job title:")) {
  //       workExperience.setPosition(
  //         line.substring("Job title:".length()).trim()
  //       );
  //     } else if (line.startsWith("Company:")) {
  //       workExperience.setCompany(line.substring("Company:".length()).trim());
  //     } else if (line.startsWith("Start date:")) {
  //       workExperience.setStartDate(
  //         line.substring("Start date:".length()).trim()
  //       );
  //     } else if (line.startsWith("End date:")) {
  //       workExperience.setEndDate(line.substring("End date:".length()).trim());
  //     } else if (line.startsWith("Description:")) {
  //       workExperience.setDescription(
  //         line.substring("Description:".length()).trim()
  //       );
  //     }
  //   }

  //   return workExperience;
  // }
}
