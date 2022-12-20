package com.oops.solvermanager.Requests;

public class ProblemRequest {
    private String problemID;
    private String dataID;
    private SolverBody[] solversToUse;
    private String userID;

    public ProblemRequest() {
    }

    public ProblemRequest(String problemID, String dataID, SolverBody[] solversToUse, String userID) {
        this.problemID = problemID;
        this.dataID = dataID;
        this.solversToUse = solversToUse;
        this.userID = userID;

    }

    public String getDataID() {
        return dataID;
    }
    public String getProblemID() {
        return problemID;
    }
    public SolverBody[] getSolversToUse() {
        return solversToUse;
    }
    public String getUserID() {
        return userID;
    }
    
}
