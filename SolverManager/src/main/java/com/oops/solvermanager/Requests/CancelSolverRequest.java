package com.oops.solvermanager.Requests;

public class CancelSolverRequest {
    private String ProblemID;
    private String UserID;

    CancelSolverRequest() {
    }

    public String getProblemID() {
        return ProblemID;
    }

    public String getUserID() {
        return UserID;
    }
}
