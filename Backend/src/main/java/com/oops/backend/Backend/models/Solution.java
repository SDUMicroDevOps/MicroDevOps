package com.oops.backend.Backend.models;

import java.sql.Date;

public class Solution {

    private String taskId;
    private String user;
    private String content;
    private Date date;
    private boolean isOptimal;

    Solution() {
    }

    public Solution(String taskId, String user, String content, Date date, boolean isOptimal) {
        this.taskId = taskId;
        this.user = user;
        this.content = content;
        this.date = date;
        this.isOptimal = isOptimal;
    }

    public String getTaskId() {
        return taskId;
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

    public boolean getIsOptimal() {
        return isOptimal;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public void setIsOptimal(boolean isOptimal) {
        this.isOptimal = isOptimal;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Solution))
            return false;
        Solution other = (Solution) obj;
        if (!(this.taskId.equals(other.taskId) &&
                this.user.equals(other.user) &&
                this.content.equals(other.content) &&
                this.date.equals(other.date) &&
                this.isOptimal == other.isOptimal))
            return false;
        return true;
    }
}