package com.oops.backend.Backend.models;

public class BucketResponse {
    private String TaskID;
    private String ProblemFileUrl;
    private String DataFileUrl;
    private String MethodAllowed;

    BucketResponse() {
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String TaskID) {
        this.TaskID = TaskID;
    }

    public String getProblemFileUrl() {
        return ProblemFileUrl;
    }

    public void setProblemFileUrl(String ProblemFileUrl) {
        this.ProblemFileUrl = ProblemFileUrl;
    }

    public String getDataFileUrl() {
        return DataFileUrl;
    }

    public void setDataFileUrl(String DataFileUrl) {
        this.DataFileUrl = DataFileUrl;
    }

    public String getMethodAllowed() {
        return MethodAllowed;
    }

    public void setMethodAllowed(String MethodAllowed) {
        this.MethodAllowed = MethodAllowed;
    }

}