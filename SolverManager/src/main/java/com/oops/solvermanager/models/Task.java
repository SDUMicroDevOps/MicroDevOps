package com.oops.solvermanager.models;

import java.sql.Date;

public class Task {
    private String username;
    private int solver;
    private String taskId;
    private Date solverTimeStamp;
    private int maxMemory;
    private String mzn;
    private String dzn;
    private int vcpu;

    public Task() {
    }

    public String getUsername() {
        return username;
    }

    public String getDzn() {
        return dzn;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public String getMzn() {
        return mzn;
    }

    public int getSolver() {
        return solver;
    }

    public Date getSolverTimeStamp() {
        return solverTimeStamp;
    }

    public String getTaskId() {
        return taskId;
    }

    public int getVcpu() {
        return vcpu;
    }
}
