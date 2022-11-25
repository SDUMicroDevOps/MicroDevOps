package com.oops.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.oops.app.responseType.AvailableSolver;
import com.oops.app.responseType.Privilage;
import com.oops.app.responseType.Solution;
import com.oops.app.responseType.User;

import requestType.SolutionRequest;

public class SQLController {
    
    private final String ip;
    private final String user;
    private final String pwd;

    public SQLController(String ip, String db, String user, String pwd) {
        this.ip = "jdbc:mysql://" + ip + "/" + db;
        this.user = user;
        this.pwd = pwd;
    }

    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(ip, user, pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from User");

            while(rs.next()) {
                User user = new User(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
                userList.add(user);
            }
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public User getUser(String username) {
        User user = null;
        try {
            Connection conn = DriverManager.getConnection(ip, this.user, pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from User where username='" + username + "'");

            while(rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User addUser(User newUser) {
        try {
            Connection conn = DriverManager.getConnection(ip, this.user, pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("insert into User (username, password, privilege_id, vCPULimit) values ('" + newUser.getUsername() + "','" + newUser.getPwd() + "','" + newUser.getPrivilege_id() + "','" + newUser.getVCPULimit() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return newUser;
    }

    public int deleteUser(String username) {
        try {
            Connection conn = DriverManager.getConnection(ip, user, pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from User where username='" + username + "'");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 200;
    }

    public List<Privilage> getAllPrivilages() {
        return null;
    }

    public Privilage getPrivilage(int id) {
        return null;
    }

    public Privilage addPrivilage(String newRoleName) {
        return null;
    }

    public int deletePrivilage(int id) {
        return 0;
    }

    public List<AvailableSolver> getAllSolvers() {
        return null;
    }

    public AvailableSolver getSolver(int id) {
        return null;
    }

    public AvailableSolver addSolver(String solverName) {
        return null;
    }

    public int deleteSolver(int id) {
        return 0;
    }

    public List<Solution> getAllSolutions() {
        return null;
    }

    public Solution getSolution(int id) {
        return null;
    }

    public int deleteSolution(int id) {
        return 0;
    }

    public Solution addSolution(SolutionRequest newsolutionName) {
        return null;
    }
}
