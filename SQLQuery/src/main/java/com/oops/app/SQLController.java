package com.oops.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.oops.app.responseType.Solver;
import com.oops.app.responseType.TaskQueue;
import com.oops.app.responseType.Privilage;
import com.oops.app.responseType.Solution;
import com.oops.app.responseType.User;

public class SQLController {
    
    private DataSource pool;

    public SQLController() {
        pool = SQLCloudController.createConnectionPool();
    }

    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();

        try {
            Connection conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user");

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
            ResultSet rs = stmt.executeQuery("select * from user where username='" + username + "'");

            while(rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User addUser(User newUser) {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO `user`(`username`, `pwd`, `privilage_id`, `vCPU_limit`) VALUES ('%s','%s','%d','%d')", newUser.getUsername(), newUser.getPwd(), newUser.getPrivilege_id(), newUser.getVCPULimit()));
            conn.close();
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        return newUser;
    }

    public int deleteUser(String username) {
        try {
            Connection conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from user where username='" + username + "'");
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
            return 400;
        }
        return 200;
    }

    public List<Privilage> getAllPrivilages() {
        List<Privilage> privilages = new ArrayList<>();
        try {
            Connection conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `privilage`");

            while(rs.next()) {
                Privilage privilage = new Privilage(rs.getInt(1), rs.getString(2));
                privilages.add(privilage);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privilages;
    }

    public Privilage getPrivilage(int id) {
        Privilage privilage = null;
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `privilage` WHERE id=" + id);
            while(rs.next()) {
                privilage = new Privilage(id, rs.getString(2));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privilage;
    }

    public Privilage addPrivilage(String newRoleName) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO `privilage`(`role_name`) VALUES ('%s')", newRoleName));
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public int deletePrivilage(int id) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM `privilage` WHERE id=%d", id));
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 400;
        }
        return 200;
    }

    public List<Solver> getAllSolvers() {
        List<Solver> solvers = new ArrayList<>();
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `solver`");
            while(rs.next()) {
                solvers.add(new Solver(rs.getInt(1), rs.getString(2)));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return solvers;
    }

    public Solver getSolver(int id) {
        Solver solver = null;
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `solver` WHERE id=" + id);
            while(rs.next()) {
                solver = new Solver(rs.getInt(1), rs.getString(2));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return solver;
    }

    public Solver addSolver(String solverName) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO `solver`(`solver_name`) VALUES ('%s')", solverName));
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public int deleteSolver(int id) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM `solver` WHERE id=%d", id));
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 400;
        }
        return 200;
    }

    public List<Solution> getAllSolutions() {
        List<Solution> solutions = new ArrayList<>();
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `solution`");
            while(rs.next()) {
                solutions.add(new Solution(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getBoolean(5)));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return solutions;
    }

    public Solution getSolution(String taskId) {
        Solution solution = null;
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `solution` WHERE task_id='%s'", taskId));
            while(rs.next()) {
                solution = new Solution(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getBoolean(5));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return solution;
    }

    public int deleteSolution(String taskId) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM `solution` WHERE task_id='%s' AND is_optimal != 1", taskId));
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 400;
        }
        return 200;
    }

    public Solution addSolution(Solution newSolutionName) {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO `solution`(`task_id`, `user`, `content`, `date`, `is_optimal`) VALUES ('%s','%s','%s','%s','%d')", 
                newSolutionName.getTaskId(), newSolutionName.getUser(), newSolutionName.getContent(), newSolutionName.getDate(), newSolutionName.getIsOptimal() ? 1 : 0 ));
            conn.close();
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<TaskQueue> getAllQueuedTask() {
        List<TaskQueue> tasks = new ArrayList<>();
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `task_queue` ORDER BY user");
            while(rs.next()) {
                tasks.add(new TaskQueue(
                    rs.getString(1), 
                    rs.getInt(2), 
                    rs.getString(3), 
                    rs.getTimestamp(4), 
                    rs.getInt(5),
                    rs.getInt(6), 
                    rs.getInt(7), 
                    rs.getString(8), 
                    rs.getString(9)));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tasks;
    }

    public List<TaskQueue> getQueuedTaskByUser(String username) {
        List<TaskQueue> tasks = new ArrayList<>();
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `task_queue` WHERE user='%s' ORDER BY solver_timestamp", username));
            while(rs.next()) {
                tasks.add(new TaskQueue(
                    rs.getString(1), 
                    rs.getInt(2), 
                    rs.getString(3), 
                    rs.getTimestamp(4),
                    rs.getInt(5),
                    rs.getInt(6), 
                    rs.getInt(7), 
                    rs.getString(8), 
                    rs.getString(9)));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tasks;
    }

    public List<TaskQueue> getQueuedTaskByTaskId(String taskId) {
        List<TaskQueue> tasks = new ArrayList<>();
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `task_queue` WHERE task_id='%s' ORDER BY solver_timestamp", taskId));
            while(rs.next()) {
                tasks.add(new TaskQueue(
                    rs.getString(1), 
                    rs.getInt(2), 
                    rs.getString(3), 
                    rs.getTimestamp(4), 
                    rs.getInt(5), 
                    rs.getInt(6), 
                    rs.getInt(7), 
                    rs.getString(8), 
                    rs.getString(9)));
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tasks;
    }

    public int addTask(TaskQueue taskQueue) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO `task_queue`(`user`, `solver`, `task_id`, `solver_timestamp`, `timeout`, `vCPU`, `max_memory`, `mzn`, `dzn`) VALUES ('%s','%d','%s','%s','%d','%d','%d','%s','%s')", 
                taskQueue.getUsername(), 
                taskQueue.getSolver(), 
                taskQueue.getTaskId(), 
                taskQueue.getSolverTimestamp(),
                taskQueue.getTimeout(),
                taskQueue.getVCPU(), 
                taskQueue.getMaxMemory(), 
                taskQueue.getMzn(), 
                taskQueue.getDzn()));
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 400;
        }
        return 200;
    }

    public int deleteTask(String taskId) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM `task_queue` WHERE task_id='%s'", taskId));
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 400;
        }
        return 200;
    }

    public Solver getSolverByName(String name) {
        Solver solver = null;
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(String.format("SELECT * FROM `solver` WHERE id='%s'", name));
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solver;
    }

    public int changeVCPULimit(String username, int vcpuLimit) {
        try (Connection conn = pool.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(String.format("UPDATE `user` SET `vCPU_limit`='%d' WHERE username='%s'", vcpuLimit, username));
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 400;
        }
        return 200;
    }
}
