package com.oops.app.requestType;

public class SolverRequest {
    
    private String solverName;

    SolverRequest() {}
    public SolverRequest(String solverName) {
        this.solverName = solverName;
    }

    public String getSolverName() {
        return this.solverName;
    }

    public void setSolverName(String solverName) {
        this.solverName = solverName;
    }
}
