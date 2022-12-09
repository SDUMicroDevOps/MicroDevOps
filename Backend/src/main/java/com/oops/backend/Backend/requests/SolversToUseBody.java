package com.oops.backend.Backend.requests;

public class SolversToUseBody {
    private String numberVCPU;
    private String maxMemory;
    private String timeOutPeriod;

    public SolversToUseBody(String numberVCPU, String maxMemory, String timeOutPeriod) {
        this.numberVCPU = numberVCPU;
        this.maxMemory = maxMemory;
        this.timeOutPeriod = timeOutPeriod;
    }

    public String getNumberVCPU() {
        return numberVCPU;
    }

    public void setNumberVCPU(String numberVCPU) {
        this.numberVCPU = numberVCPU;
    }

    public String getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(String maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getTimeOutPeriod() {
        return timeOutPeriod;
    }

    public void setTimeOutPeriod(String timeOutPeriod) {
        this.timeOutPeriod = timeOutPeriod;
    }

    
}
