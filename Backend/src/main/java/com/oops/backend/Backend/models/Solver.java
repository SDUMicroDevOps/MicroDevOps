package com.oops.backend.Backend.models;

public class Solver {

    private int id;
    private String solverName;

    Solver() {
    }

    public Solver(int id, String solverName) {
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

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Solver))
            return false;
        Solver other = (Solver) obj;
        if (!(this.id == other.id && this.solverName.equals(other.solverName)))
            return false;
        return true;
    }
}