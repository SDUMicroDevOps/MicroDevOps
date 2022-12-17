package com.oops.solvermanager.Requests;

public class CancelTaskRequest {
    private String UserID;

    public CancelTaskRequest() {
    }

    public CancelTaskRequest(String userID) {
        this.UserID = userID;
    }

    public String getUserId() {
        return UserID;
    }
}
