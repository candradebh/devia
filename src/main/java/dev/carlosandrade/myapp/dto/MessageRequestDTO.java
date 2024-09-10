package dev.carlosandrade.myapp.dto;

public class MessageRequestDTO {
    private Long projectId;
    private String message;

    // Getters e Setters
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

