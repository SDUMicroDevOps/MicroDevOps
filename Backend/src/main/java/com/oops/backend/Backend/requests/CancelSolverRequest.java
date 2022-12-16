package com.oops.backend.Backend.requests;

public class CancelSolverRequest {
    private String problemID;
    private String userID;

    public CancelSolverRequest(String ProblemID, String UserID) {
        this.problemID = ProblemID;
        this.userID = UserID;
    }

    public String getProblemID() {
        return problemID;
    }

    public String getUserID() {
        return userID;
    }
}
