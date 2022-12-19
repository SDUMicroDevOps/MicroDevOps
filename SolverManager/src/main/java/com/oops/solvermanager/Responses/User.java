package com.oops.solvermanager.Responses;

public class User {
    private String username;
    private String pwd;
    private int privilege_id;
    private int vcpulimit;

    public User() {
    }

    public User(String username, String pwd, int privilege_id, int vcpulimit) {
        this.username = username;
        this.pwd = pwd;
        this.privilege_id = privilege_id;
        this.vcpulimit = vcpulimit;
    }

    public int getVcpulimit() {
        return vcpulimit;
    }

    public String getPassword() {
        return pwd;
    }

    public int getPrivilege_id() {
        return privilege_id;
    }

    public String getUsername() {
        return username;
    }
}
