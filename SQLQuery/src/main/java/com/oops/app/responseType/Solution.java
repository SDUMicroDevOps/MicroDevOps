package com.oops.app.responseType;

public class Solution {
    
    private int id;
    private String user;
    private String content;
    private String date;

    Solution() {}
    public Solution(int id, String user, String content, String date) {
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

    public String getDate() {
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

    public void setDate(String date) {
        this.date = date;
    }
}
