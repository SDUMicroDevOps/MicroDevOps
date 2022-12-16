package com.oops.backend.Backend.requests;

public class SolversToUseBody {
    private String NumberVCPU;
    private String MaxMemory;
    private String TimeOutPeriod;

    public SolversToUseBody(String NumberVCPU, String MaxMemory, String TimeOutPeriod) {
        this.NumberVCPU = NumberVCPU;
        this.MaxMemory = MaxMemory;
        this.TimeOutPeriod = TimeOutPeriod;
    }

    public String getNumberVCPU() {
        return NumberVCPU;
    }

    public void setNumberVCPU(String NumberVCPU) {
        this.NumberVCPU = NumberVCPU;
    }

    public String getMaxMemory() {
        return MaxMemory;
    }

    public void setMaxMemory(String MaxMemory) {
        this.MaxMemory = MaxMemory;
    }

    public String getTimeOutPeriod() {
        return TimeOutPeriod;
    }

    public void setTimeOutPeriod(String TimeOutPeriod) {
        this.TimeOutPeriod = TimeOutPeriod;
    }

}
