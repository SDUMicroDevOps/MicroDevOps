package com.oops.backend.Backend.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolversToUseBody {
    @JsonProperty("numberVCPU")
    private int numberVCPU;

    @JsonProperty("maxMemory")
    private int maxMemory;

    @JsonProperty("timeOut")
    private int timeOut;

    @JsonProperty("solverName")
    private String solverName;

    public SolversToUseBody() {
    }

    public SolversToUseBody(String solverName, int numberVCPU, int maxMemory, int timeOut) {
        this.solverName = solverName;
        this.numberVCPU = numberVCPU;
        this.maxMemory = maxMemory;
        this.timeOut = timeOut;
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
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getSolverName() {
        return solverName;
    }

    public void setSolverName(String solverName) {
        this.solverName = solverName;
    }

}
