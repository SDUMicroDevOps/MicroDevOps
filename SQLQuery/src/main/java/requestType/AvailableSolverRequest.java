package requestType;

public class AvailableSolverRequest {
    
    private String solverName;

    AvailableSolverRequest() {}
    public AvailableSolverRequest(String solverName) {
        this.solverName = solverName;
    }

    public String getSolverName() {
        return this.solverName;
    }

    public void setSolverName(String solverName) {
        this.solverName = solverName;
    }
}
