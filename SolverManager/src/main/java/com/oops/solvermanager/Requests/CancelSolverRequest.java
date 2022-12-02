package com.oops.solvermanager.Requests;

public class CancelSolverRequest {
    private String problemID;
    private String userID;

    CancelSolverRequest() {
    }

    public String getProblemID() {
        return problemID;
    }

    public String getUserID() {
        return userID;
    }
}
