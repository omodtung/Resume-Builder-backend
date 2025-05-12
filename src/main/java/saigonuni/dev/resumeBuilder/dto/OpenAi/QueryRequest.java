package saigonuni.dev.resumeBuilder.dto.OpenAi;

import lombok.Data;

@Data
public class QueryRequest {

    private String query;
    private String conversationId; // Keep this if your consumer might use it

    // Default constructor (required by Jackson)
    public QueryRequest() {}

    public QueryRequest(String query) {
        this.query = query;
    }

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

}

