package com.oops.backend.Backend.models;

public class User {

    private String username;
    private String pwd;
    private int privilege_id;
    private int vCPULimit;

    User() {
    }

    public User(String username, String pwd, int privilege_id, int vCPULimit) {
        this.username = username;
        this.pwd = pwd;
        this.privilege_id = privilege_id;
        this.vCPULimit = vCPULimit;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPwd() {
        return this.pwd;
    }

    public int getPrivilege_id() {
        return this.privilege_id;
    }

    public int getVCPULimit() {
        return this.vCPULimit;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setPrivilege_id(int privilege_id) {
        this.privilege_id = privilege_id;
    }

    public void setVCPULimit(int vCPULimit) {
        this.vCPULimit = vCPULimit;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        if (!(this.username.equals(other.username) &&
                this.pwd.equals(other.pwd) &&
                this.privilege_id == other.privilege_id &&
                this.vCPULimit == other.vCPULimit))
            return false;
        return true;
    }
}