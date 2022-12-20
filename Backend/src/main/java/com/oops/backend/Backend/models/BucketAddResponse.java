package com.oops.backend.Backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BucketAddResponse {
    private String ProblemFileUrl;
    private String DataFileUrl;

    BucketAddResponse() {
    }

    public BucketAddResponse(String problemFileUrl, String dataFileUrl) {
        ProblemFileUrl = problemFileUrl;
        DataFileUrl = dataFileUrl;
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

}
