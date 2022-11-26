package com.oops.app.requestType;

public class SolutionRequest {
    private String user;
    private String content;
    private String date;

    SolutionRequest() {}
    public SolutionRequest(String user, String content, String date) {
        this.user = user;
        this.content = content;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
