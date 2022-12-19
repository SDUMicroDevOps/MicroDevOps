package com.oops.backend.Backend.requests;

public class SolveRequest {
    private String ProblemID;
    private String DataID;
    private SolversToUseBody[] SolversToUse;
    private String UserID;

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
