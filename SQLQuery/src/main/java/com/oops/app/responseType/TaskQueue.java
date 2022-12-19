package com.oops.app.responseType;

import java.sql.Timestamp;

public class TaskQueue {
    private String username;
    private int solver;
    private String taskId;
    private Timestamp solverTimestamp;
    private int timeout;
    private int vCPU;
    private int maxMemory;
    private String mzn;
    private String dzn;

    TaskQueue() {}
    public TaskQueue(String username,
    int solver,
    String taskId,
    Timestamp solverTimestamp,
    int timeout,
    int vCPU,
    int maxMemory,
    String mzn,
    String dzn) {
        this.username = username;
        this.solver = solver;
        this.taskId = taskId;
        this.solverTimestamp = solverTimestamp;
        this.timeout = timeout;
        this.vCPU = vCPU;
        this.maxMemory = maxMemory;
        this.mzn = mzn;
        this.dzn = dzn;
    }

    public String getUsername() {
        return username;
    }

    public int getSolver() {
        return solver;
    }

    public String getTaskId() {
        return taskId;
    }

    public Timestamp getSolverTimestamp() {
        return solverTimestamp;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getVCPU() {
        return vCPU;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public String getMzn() {
        return mzn;
    }

    public String getDzn() {
        return dzn;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSolver(int solver) {
        this.solver = solver;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setSolverTimestamp(Timestamp solverTimestamp) {
        this.solverTimestamp = solverTimestamp;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setVCPU(int vCPU) {
        this.vCPU = vCPU;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public void setMzn(String mzn) {
        this.mzn = mzn;
    }

    public void setDzn(String dzn) {
        this.dzn = dzn;
    }

    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof TaskQueue)) return false;
        TaskQueue other = (TaskQueue) obj;
        if(!(this.username.equals(other.username) && 
            this.solver == other.solver && 
            this.taskId.equals(other.taskId) && 
            this.solverTimestamp.equals(other.solverTimestamp) && 
            this.vCPU == other.vCPU && 
            this.maxMemory == other.maxMemory && 
            this.mzn.equals(other.mzn) && 
            this.dzn.equals(other.dzn))) return false;
        return true;
    }
}
