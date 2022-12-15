package com.oops.backend.Backend.requests;

public class CancelUserTasksRequest {
    private String userID;

    public CancelUserTasksRequest(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}