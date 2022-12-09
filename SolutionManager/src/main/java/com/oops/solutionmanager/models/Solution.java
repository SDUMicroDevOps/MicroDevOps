package com.oops.solutionmanager.models;

public class Solution {

    private String taskID;
    private String userID;
    private String solution;
    private Boolean isOptimal;

    public Solution(String taskID, String userID, String solution, Boolean isOptimal) {
        this.taskID = taskID;
        this.userID = userID;
        this.solution = solution;
        this.isOptimal = isOptimal;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String content) {
        this.solution = content;
    }

    public Boolean getOptimal() {
        return isOptimal;
    }

    public void setOptimal(Boolean optimal) {
        isOptimal = optimal;
    }
}
