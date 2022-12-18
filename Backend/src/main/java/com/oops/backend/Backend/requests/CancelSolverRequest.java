package com.oops.backend.Backend.requests;

public class CancelSolverRequest {
    private String ProblemID;
    private String UserID;

    public CancelSolverRequest(String ProblemID, String UserID) {
        this.ProblemID = ProblemID;
        this.UserID = UserID;
    }

    public String getProblemID() {
        return ProblemID;
    }

    public String getUserID() {
        return UserID;
    }
}
