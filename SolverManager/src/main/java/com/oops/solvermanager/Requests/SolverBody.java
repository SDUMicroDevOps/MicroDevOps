package com.oops.solvermanager.Requests;

public class SolverBody {
    private String solverName;
        private int numberVCPU;
        private int maxMemory; // Max Memory in MB
        private int timeout; // Seconds to timeout

        SolverBody() {
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

        public int getTimeout() {
            return timeout;
        }
    }
