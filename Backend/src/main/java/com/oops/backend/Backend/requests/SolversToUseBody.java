package com.oops.backend.Backend.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolversToUseBody {
    @JsonProperty("numberVCPU")
    private int numberVCPU;

    @JsonProperty("maxMemory")
    private int maxMemory;

    @JsonProperty("timeout")
    private int timeout;

    @JsonProperty("solverName")
    private String solverName;

    public SolversToUseBody() {
    }

    public SolversToUseBody(String solverName, int numberVCPU, int maxMemory, int timeout) {
        this.solverName = solverName;
        this.numberVCPU = numberVCPU;
        this.maxMemory = maxMemory;
        this.timeout = timeout;
    }

    public int getNumberVCPU() {
        return numberVCPU;
    }

    public void setNumberVCPU(int numberVCPU) {
        this.numberVCPU = numberVCPU;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public int getTimeOut() {
        return timeout;
    }

    public void setTimeOut(int timeout) {
        this.timeout = timeout;
    }

    public String getSolverName() {
        return solverName;
    }

    public void setSolverName(String solverName) {
        this.solverName = solverName;
    }

}
