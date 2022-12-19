package com.oops.backend.Backend.models;

import java.sql.Timestamp;

public class TaskQueue {
    private String UserName;
    private int Solver;
    private String TaskID;
    private Timestamp SolverTimestamp;
    private int vCPU;
    private String Mzn;
    private String Dzn;

    TaskQueue() {
    }

    public TaskQueue(String UserName,
            int Solver,
            String TaskID,
            Timestamp SolverTimestamp,
            int vCPU,
            String Mzn,
            String Dzn) {
        this.UserName = UserName;
        this.Solver = Solver;
        this.TaskID = TaskID;
        this.SolverTimestamp = SolverTimestamp;
        this.vCPU = vCPU;
        this.Mzn = Mzn;
        this.Dzn = Dzn;
    }

    public String getUserName() {
        return UserName;
    }

    public int getSolver() {
        return Solver;
    }

    public String getTaskID() {
        return TaskID;
    }

    public Timestamp getSolverTimestamp() {
        return SolverTimestamp;
    }

    public int getVCPU() {
        return vCPU;
    }

    public String getMzn() {
        return Mzn;
    }

    public String getDzn() {
        return Dzn;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void setSolver(int Solver) {
        this.Solver = Solver;
    }

    public void setTaskID(String TaskID) {
        this.TaskID = TaskID;
    }

    public void setSolverTimestamp(Timestamp SolverTimestamp) {
        this.SolverTimestamp = SolverTimestamp;
    }

    public void setVCPU(int vCPU) {
        this.vCPU = vCPU;
    }

    public void setMzn(String Mzn) {
        this.Mzn = Mzn;
    }

    public void setDzn(String Dzn) {
        this.Dzn = Dzn;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TaskQueue))
            return false;
        TaskQueue other = (TaskQueue) obj;
        if (!(this.UserName.equals(other.UserName) &&
                this.Solver == other.Solver &&
                this.TaskID.equals(other.TaskID) &&
                this.SolverTimestamp.equals(other.SolverTimestamp) &&
                this.vCPU == other.vCPU &&
                this.TaskID == other.TaskID &&
                this.Mzn.equals(other.Mzn) &&
                this.Dzn.equals(other.Dzn)))
            return false;
        return true;
    }
}
