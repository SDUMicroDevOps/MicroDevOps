package com.oops.backend.Backend.requests;

public class SolversToUseBody {
    private int NumberVCPU;
    private int MaxMemory;
    private int TimeOutPeriod;
    private String SolverName;

    public SolversToUseBody(String SolverName, int NumberVCPU, int MaxMemory, int TimeOutPeriod) {
        this.SolverName = SolverName;
        this.NumberVCPU = NumberVCPU;
        this.MaxMemory = MaxMemory;
        this.TimeOutPeriod = TimeOutPeriod;
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

    public int getTimeOutPeriod() {
        return TimeOutPeriod;
    }

    public void setTimeOutPeriod(int TimeOutPeriod) {
        this.TimeOutPeriod = TimeOutPeriod;
    }

}
