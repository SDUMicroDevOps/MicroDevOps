package com.oops.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.oops.app.requestType.SolutionRequest;
import com.oops.app.responseType.AvailableSolver;
import com.oops.app.responseType.Privilage;
import com.oops.app.responseType.Solution;
import com.oops.app.responseType.User;

public class SQLController {
    
    private DataSource pool;

    public SQLController(String ip, String db, String user, String pwd) {
        pool = SQLCloudController.createConnectionPool();
    }

    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();

        try {
            Connection conn = pool.getConnection();
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
            Connection conn = pool.getConnection();
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
            Connection conn = pool.getConnection();
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
            Connection conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from User where username='" + username + "'");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 200;
    }

    public List<Privilage> getAllPrivilages() {
        List<Privilage> privilages = new ArrayList<>();
        try {
            Connection conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Privilages");

            while(rs.next()) {
                Privilage privilage = new Privilage(rs.getInt(1), rs.getString(2));
                privilages.add(privilage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privilages;
    }

    public Privilage getPrivilage(int id) {
        Privilage privilage = null;
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Privilages where id=" + id);
            while(rs.next()) {
                privilage = new Privilage(id, rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privilage;
    }

    public Privilage addPrivilage(String newRoleName) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO Privilages(roleName) VALUES ('%s')", newRoleName));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public int deletePrivilage(int id) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM Privilages WHERE id=%d", id));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 200;
    }

    public List<AvailableSolver> getAllSolvers() {
        List<AvailableSolver> solvers = new ArrayList<>();
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AvailableSolvers");
            while(rs.next()) {
                solvers.add(new AvailableSolver(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return solvers;
    }

    public AvailableSolver getSolver(int id) {
        AvailableSolver solver = null;
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AvailableSolvers where id=" + id);
            while(rs.next()) {
                solver = new AvailableSolver(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return solver;
    }

    public AvailableSolver addSolver(String solverName) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO AvailableSolvers(solver_name) VALUES ('%s')", solverName));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public int deleteSolver(int id) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM `AvailableSolvers` WHERE id=%d", id));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 200;
    }

    public List<Solution> getAllSolutions() {
        List<Solution> solutions = new ArrayList<>();
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Solutions");
            while(rs.next()) {
                solutions.add(new Solution(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4)));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return solutions;
    }

    public Solution getSolution(int id) {
        Solution solution = null;
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Solutions WHERE=" + id);
            while(rs.next()) {
                solution = new Solution(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return solution;
    }

    public int deleteSolution(int id) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM `Solutions` WHERE id=%d", id));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 200;
    }

    public Solution addSolution(SolutionRequest newSolutionName) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO Solutions(user_id, content, time_created) VALUES ('%s','%s','%t')", newSolutionName.getUser(), newSolutionName.getContent(), newSolutionName.getDate()));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
