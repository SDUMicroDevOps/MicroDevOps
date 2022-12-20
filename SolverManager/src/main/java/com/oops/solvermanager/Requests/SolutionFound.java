package com.oops.solvermanager.Requests;

public class SolutionFound {
    private String userID;

    public SolutionFound() {
    }
    public SolutionFound(String userID){
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
