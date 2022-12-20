package com.oops.backend.Backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BucketResponse {
    @JsonProperty("TaskID")
    private String TaskID;

    @JsonProperty("ProblemFileUrl")
    private String ProblemFileUrl;

    @JsonProperty("DataFileUrl")
    private String DataFileUrl;

    @JsonProperty("MethodAllowed")
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