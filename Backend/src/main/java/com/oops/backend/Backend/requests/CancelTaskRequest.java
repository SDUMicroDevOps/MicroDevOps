package com.oops.backend.Backend.requests;

public class CancelTaskRequest {
    private String userId;

    public CancelTaskRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
