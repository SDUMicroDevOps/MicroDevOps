package com.oops.backend.Backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolverBody {
    @JsonProperty("SolverName")
    private String SolverName;
    @JsonProperty("NumberVCPU")
    private int NumberVCPU;
    @JsonProperty("MaxMemory")
    private int MaxMemory; // Max Memory in MB
    @JsonProperty("TimeOut")
    private int Timeout; // Seconds to Timeout

    public SolverBody() {
    }

    public SolverBody(int MaxMemory, int NumberVCPU, int Timeout, String SolverName) {
        this.MaxMemory = MaxMemory;
        this.NumberVCPU = NumberVCPU;
        this.Timeout = Timeout;
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
        return Timeout;
    }
}
