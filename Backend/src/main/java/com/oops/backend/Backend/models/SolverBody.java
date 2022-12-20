package com.oops.backend.Backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolverBody {
    @JsonProperty("solverName")
    private String solverName;
    @JsonProperty("numberVCPU")
    private int numberVCPU;
    @JsonProperty("maxMemory")
    private int maxMemory; // Max Memory in MB
    @JsonProperty("timeout")
    private int timeout; // Seconds to timeOut

    public SolverBody() {
    }

    public SolverBody(int maxMemory, int numberVCPU, int timeout, String solverName) {
        this.maxMemory = maxMemory;
        this.numberVCPU = numberVCPU;
        this.timeout = timeout;
        this.solverName = solverName;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public int getNumberVCPU() {
        return numberVCPU;
    }

    public String getSolverName() {
        return solverName;
    }

    public int getTimeOut() {
        return timeout;
    }
}
