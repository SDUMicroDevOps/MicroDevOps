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

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getProblemFileUrl() {
        return ProblemFileUrl;
    }

    public void setProblemFileUrl(String problemFileUrl) {
        ProblemFileUrl = problemFileUrl;
    }

    public String getDataFileUrl() {
        return DataFileUrl;
    }

    public void setDataFileUrl(String dataFileUrl) {
        DataFileUrl = dataFileUrl;
    }

    public String getMethodAllowed() {
        return MethodAllowed;
    }

    public void setMethodAllowed(String methodAllowed) {
        MethodAllowed = methodAllowed;
    }

}