package com.example.turfly;

public class FeedbackModel {
    String id, name, email, feedback;

    public FeedbackModel() {
        // Default constructor required for Firebase
    }

    public FeedbackModel(String id, String name, String email, String feedback) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.feedback = feedback;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getFeedback() { return feedback; }
}
