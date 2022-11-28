package com.oops.app.responseType;

import java.sql.Date;

public class Solution {
    
    private int id;
    private String user;
    private String content;
    private Date date;

    Solution() {}
    public Solution(int id, String user, String content, Date date) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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
