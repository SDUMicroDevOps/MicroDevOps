package com.oops.solvermanager.Requests;

public class CancelUserTasksRequest {
    private String UserToCancel;

    public CancelUserTasksRequest() {
    }

    public CancelUserTasksRequest(String UserToCancel) {
        this.UserToCancel = UserToCancel;
    }

    public String getUserID() {
        return UserToCancel;
    }
}
