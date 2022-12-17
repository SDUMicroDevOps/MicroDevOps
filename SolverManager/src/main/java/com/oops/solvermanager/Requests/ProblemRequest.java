package com.oops.solvermanager.Requests;

public class ProblemRequest {
    private String ProblemID;
    private String DataID;
    private SolverBody[] SolversToUse;
    private String UserID;

    ProblemRequest() {
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
