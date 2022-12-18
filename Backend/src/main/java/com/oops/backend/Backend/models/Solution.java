package com.oops.backend.Backend.models;

import java.sql.Date;

public class Solution {

    private String TaskId;
    private String User;
    private String Content;
    private Date Date;
    private boolean IsOptimal;

    Solution() {
    }

    public Solution(String TaskId, String User, String Content, Date Date, boolean IsOptimal) {
        this.TaskId = TaskId;
        this.User = User;
        this.Content = Content;
        this.Date = Date;
        this.IsOptimal = IsOptimal;
    }

    public String getTaskId() {
        return TaskId;
    }

    public String getUser() {
        return User;
    }

    public String getContent() {
        return Content;
    }

    public Date getDate() {
        return Date;
    }

    public boolean getIsOptimal() {
        return IsOptimal;
    }

    public void setTaskId(String taskId) {
        this.TaskId = taskId;
    }

    public void setUser(String user) {
        this.User = user;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public void setDate(Date date) {
        this.Date = date;
    }

    public void setIsOptimal(boolean isOptimal) {
        this.IsOptimal = isOptimal;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Solution))
            return false;
        Solution other = (Solution) obj;
        if (!(this.TaskId.equals(other.TaskId) &&
                this.User.equals(other.User) &&
                this.Content.equals(other.Content) &&
                this.Date.equals(other.Date) &&
                this.IsOptimal == other.IsOptimal))
            return false;
        return true;
    }
}