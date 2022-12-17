package com.oops.solvermanager.Requests;

public class SolverBody {
    private String SolverName;
    private int NumberVCPU;
    private int MaxMemory; // Max Memory in MB
    private int TimeOut; // Seconds to timeout

    public SolverBody() {
    }

    public SolverBody(int maxMemory, int numberVCPU, int timeout, String SolverName) {
        this.MaxMemory = maxMemory;
        this.NumberVCPU = numberVCPU;
        this.TimeOut = timeout;
        this.SolverName = SolverName;
    }

    public int getMaxMemory() {
        return MaxMemory;
    }

    public int getNumberVCPU() {
        return NumberVCPU;
    }

    public String getSolverName() {
        return SolverName;
    }

    public int getTimeout() {
        return TimeOut;
    }
}
