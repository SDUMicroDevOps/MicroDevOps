package com.oops.backend.Backend.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolveRequest {
    @JsonProperty("problemID")
    private String problemID;
    @JsonProperty("dataID")
    private String dataID;
    @JsonProperty("solversToUse")
    private SolversToUseBody[] solversToUse;
    @JsonProperty("userID")
    private String userID;

    public SolveRequest() {
    }

    public SolveRequest(String problemID, String dataID, SolversToUseBody[] solversToUse, String userID) {
        this.problemID = problemID;
        this.dataID = dataID;
        this.solversToUse = solversToUse;
        this.userID = userID;
    }

    public String getProblemID() {
        return problemID;
    }

    public void setProblemID(String problemID) {
        this.problemID = problemID;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public SolversToUseBody[] getSolversToUse() {
        return solversToUse;
    }

    public void setSolversToUse(SolversToUseBody[] solversToUse) {
        this.solversToUse = solversToUse;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
