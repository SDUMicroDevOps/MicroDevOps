package com.oops.solvermanager.Responses;

public class User {
    private String username;
    private String password;
    private int privilege_id;
    private int vcpulimit;

    public User() {
    }

    public int getVcpulimit() {
        return vcpulimit;
    }

    public String getPassword() {
        return password;
    }

    public int getPrivilege_id() {
        return privilege_id;
    }

    public String getUsername() {
        return username;
    }
}
