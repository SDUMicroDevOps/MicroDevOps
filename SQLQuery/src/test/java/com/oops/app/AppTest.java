package com.oops.app;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.oops.app.responseType.Privilage;
import com.oops.app.responseType.Solution;
import com.oops.app.responseType.Solver;
import com.oops.app.responseType.TaskQueue;
import com.oops.app.responseType.User;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void privilageTest() {
        //Get all privilages
        Controller controller = new Controller();
        List<Privilage> dbPrivilages = controller.allPrivilages();
        assertTrue(dbPrivilages.size() == 3);

        //Get one privilage
        Privilage privilage1 = dbPrivilages.get(0);
        Privilage privilage2 = controller.getPrivilageById(privilage1.getId()).getBody();
        assertTrue(privilage1.equals(privilage2));
    }

    @Test
    public void solutionTest() {
        //Get all solutions
        Controller controller = new Controller();
        List<Solution> dbSolutions = controller.allSolutions();
        assertTrue(dbSolutions.size() == 2);

        //Get one solution
        Solution solution1 = dbSolutions.get(0);
        Solution solution2 = controller.getSolutionById(solution1.getTaskId()).getBody();
        assertTrue(solution1.equals(solution2));
    }

    @Test
    public void solverTest() {
        //Get all solvers
        Controller controller = new Controller();
        List<Solver> dbSolvers = controller.allSolvers();
        assertTrue(dbSolvers.size() == 3);

        //Get one solver
        Solver solver1 = dbSolvers.get(0);
        Solver solver2 = controller.getSolverById(solver1.getId()).getBody();
        assertTrue(solver1.equals(solver2));
    }

    @Test
    public void userTest() {
        //Get all users
        Controller controller = new Controller();
        List<User> dbUsers = controller.allUsers();
        assertTrue(dbUsers.size() == 3);

        //Get one user
        User user1 = dbUsers.get(0);
        User user2 = controller.getUserByName(user1.getUsername()).getBody();
        assertTrue(user1.equals(user2));
    }

    @Test
    public void taskQueueTest() {
        //Get all queue tasks for a user
        Controller controller = new Controller();
        List<TaskQueue> dbTaskQueues1 = controller.allQueuedTask("testadmin", "").getBody();
        assertTrue(dbTaskQueues1.size() == 2);
        //Get all queued tasks for a taskid
        List<TaskQueue> dbTaskQueues2 = controller.allQueuedTask("", "testadmin_2022-01-01 00:00:00").getBody();
        assertTrue(dbTaskQueues1.size() == dbTaskQueues2.size());
    }
}
