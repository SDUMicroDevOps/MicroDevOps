package com.oops.backend.Backend.requests;

public class SolversToUseBody {
    private int NumberVCPU;
    private int MaxMemory;
    private int TimeOut;
    private String SolverName;

    public SolversToUseBody(String SolverName, int NumberVCPU, int MaxMemory, int TimeOut) {
        this.SolverName = SolverName;
        this.NumberVCPU = NumberVCPU;
        this.MaxMemory = MaxMemory;
        this.TimeOut = TimeOut;
    }

    public String getSolverName() {
        return SolverName;
    }

    public void setSolverName(String solverName) {
        SolverName = solverName;
    }

    public int getNumberVCPU() {
        return NumberVCPU;
    }

    public void setNumberVCPU(int NumberVCPU) {
        this.NumberVCPU = NumberVCPU;
    }

    public int getMaxMemory() {
        return MaxMemory;
    }

    public void setMaxMemory(int MaxMemory) {
        this.MaxMemory = MaxMemory;
    }

    public int getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(int TimeOut) {
        this.TimeOut = TimeOut;
    }

}
