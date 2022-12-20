package com.oops.solvermanager.Requests;

public class CancelTaskRequest {
    private String userID;

    public CancelTaskRequest() {
    }

    public CancelTaskRequest(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
