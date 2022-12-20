package com.oops.backend.Backend.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolveRequest {
    @JsonProperty("ProblemID")
    private String ProblemID;
    @JsonProperty("DataID")
    private String DataID;
    @JsonProperty("SolversToUse")
    private SolversToUseBody[] SolversToUse;
    @JsonProperty("UserID")
    private String UserID;

    public SolveRequest() {
    }

    public SolveRequest(String ProblemID, String DataID, SolversToUseBody[] SolversToUse, String UserID) {
        this.ProblemID = ProblemID;
        this.DataID = DataID;
        this.SolversToUse = SolversToUse;
        this.UserID = UserID;
    }

    public String getProblemID() {
        return ProblemID;
    }

    public void setProblemID(String ProblemID) {
        this.ProblemID = ProblemID;
    }

    public String getDataID() {
        return DataID;
    }

    public void setDataID(String DataID) {
        this.DataID = DataID;
    }

    public SolversToUseBody[] getSolversToUse() {
        return SolversToUse;
    }

    public void setSolversToUse(SolversToUseBody[] SolversToUse) {
        this.SolversToUse = SolversToUse;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }
}
