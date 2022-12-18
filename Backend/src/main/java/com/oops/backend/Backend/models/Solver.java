package com.oops.backend.Backend.models;

public class Solver {

    private int Id;
    private String SolverName;

    Solver() {
    }

    public Solver(int Id, String SolverName) {
        this.Id = Id;
        this.SolverName = SolverName;
    }

    public int getId() {
        return this.Id;
    }

    public String getSolverName() {
        return this.SolverName;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setSolverName(String SolverName) {
        this.SolverName = SolverName;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Solver))
            return false;
        Solver other = (Solver) obj;
        if (!(this.Id == other.Id && this.SolverName.equals(other.SolverName)))
            return false;
        return true;
    }
}