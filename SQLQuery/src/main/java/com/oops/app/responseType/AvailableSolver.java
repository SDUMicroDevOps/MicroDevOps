package com.oops.app.responseType;

public class AvailableSolver {
    
    private int id;
    private String solverName;

    AvailableSolver() {}
    public AvailableSolver(int id, String solverName) {
        this.id = id;
        this.solverName = solverName;
    }

    public int getId() {
        return this.id;
    }

    public String getSolverName() {
        return this.solverName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSolverName(String solverName) {
        this.solverName = solverName;
    }
}
