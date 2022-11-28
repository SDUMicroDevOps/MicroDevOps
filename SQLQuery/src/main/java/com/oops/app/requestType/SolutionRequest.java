package com.oops.app.requestType;

import java.sql.Date;

public class SolutionRequest {
    private String user;
    private String content;
    private Date date;

    SolutionRequest() {}
    public SolutionRequest(String user, String content, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
