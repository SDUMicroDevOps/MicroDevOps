package com.oops.solvermanager.Requests;

public class ProblemRequest {
    private String problemID;
    private String dataID;
    private SolverBody[] solversToUse;

    ProblemRequest() {
    }

    public String getProblemID() {
        return problemID;
    }

    public String getDataID() {
        return dataID;
    }

    public SolverBody[] getSolversToUse() {
        return solversToUse;
    }
}
