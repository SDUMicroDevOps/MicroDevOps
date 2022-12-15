package com.oops.app.responseType;

public class Privilage {
    private int id;
    private String roleName;

    Privilage(){}

    public Privilage(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Privilage)) return false;
        Privilage other = (Privilage) obj;
        if(!(this.id == other.id && this.roleName.equals(other.roleName))) return false;
        return true;
    }
}
