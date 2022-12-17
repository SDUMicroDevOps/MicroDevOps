package com.oops.solvermanager.Requests;

public class ProblemRequest {
    private String ProblemID;
    private String DataID;
    private SolverBody[] SolversToUse;
    private String UserID;

    public ProblemRequest() {
    }

    public ProblemRequest(String ProblemID, String DataID, SolverBody[] SolversToUse, String UserID) {
        this.ProblemID = ProblemID;
        this.DataID = DataID;
        this.SolversToUse = SolversToUse;
        this.UserID = UserID;

    }

    public String getProblemID() {
        return ProblemID;
    }

    public String getDataID() {
        return DataID;
    }

    public SolverBody[] getSolversToUse() {
        return SolversToUse;
    }

    public String getUserID() {
        return UserID;
    }
}
