package com.oops.solvermanager.Requests;

public class CancelUserTasksRequest {
    private String userToCancel;

    public CancelUserTasksRequest() {
    }

    public CancelUserTasksRequest(String userToCancel) {
        this.userToCancel = userToCancel;
    }

    public String getUserToCancel() {
        return userToCancel;
    }
}
