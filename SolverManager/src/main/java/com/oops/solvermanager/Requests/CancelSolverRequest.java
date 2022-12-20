package com.oops.solvermanager.Requests;

public class CancelSolverRequest {
    private String problemID;
    private String userID;

    public CancelSolverRequest() {
    }

    public CancelSolverRequest(String problemID, String userID) {
        this.problemID = problemID;
        this.userID = userID;
    }

    public String getProblemID() {
        return problemID;
    }
    public String getUserID() {
        return userID;
    }
}
