package com.oops.solvermanager.Requests;

public class CancelSolverRequest {
    private String TaskToCancel;
    private String userID;

    CancelSolverRequest() {
    }

    public String getTaskToCancel() {
        return TaskToCancel;
    }

    public String getUserID() {
        return userID;
    }
}
