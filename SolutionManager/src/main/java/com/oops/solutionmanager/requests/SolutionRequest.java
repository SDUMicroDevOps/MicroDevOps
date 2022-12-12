package com.oops.solutionmanager.requests;


public class SolutionRequest {
    private String taskID;
    private String userID;
    private Boolean isOptimal;
    private String solution;

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

    public Boolean getOptimal() {
        return isOptimal;
    }

    public void setOptimal(Boolean optimal) {
        isOptimal = optimal;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public SolutionRequest(String taskID, String userID, Boolean isOptimal, String solution) {
        this.taskID = taskID;
        this.userID = userID;
        this.isOptimal = isOptimal;
        this.solution = solution;
    }



}
