package saigonuni.dev.resumeBuilder.dto.OpenAi;

import lombok.Data;

@Data
public class DescriptionDTO {

  private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
