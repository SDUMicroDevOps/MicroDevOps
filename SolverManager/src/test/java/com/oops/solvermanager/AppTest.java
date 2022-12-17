package com.oops.solvermanager;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.gson.Gson;
import com.oops.solvermanager.Requests.CancelTaskRequest;
import com.oops.solvermanager.Requests.ProblemRequest;
import com.oops.solvermanager.Requests.SolverBody;
import com.oops.solvermanager.Responses.User;

/**
 * Unit test for the SolverManager using wiremock
 */
@WireMockTest(httpPort = 8082)
public class AppTest {

    SolverManagerController toTest = new SolverManagerController();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8082);

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void newTaskDeleteTest() {
        Gson gson = new Gson();
        User testUser = new User("bread", "soup", 0, 4);
        stubFor(get("/users/bread")
                .willReturn(ok().withHeader("Content-Type", "application/json").withBody(gson.toJson(testUser))));
        SolverBody[] toUse = new SolverBody[1];
        toUse[0] = new SolverBody(100, 4, 100, "chuffed");
        ProblemRequest request = new ProblemRequest("testID", "testData", toUse, testUser.getUsername());
        ResponseEntity<String> response = toTest.createJob(request);
        assertTrue(response.getStatusCode() == HttpStatus.OK);
        ResponseEntity<SolverBody[]> solverResponse = toTest.getJobsForUser("bread");
        assertTrue(solverResponse.getStatusCode() == HttpStatus.OK);
        SolverBody[] solvers = solverResponse.getBody();
        assertTrue(solvers.length == 1);
        CancelTaskRequest cancelReq = new CancelTaskRequest("bread");
        response = toTest.cancelTask(request.getProblemID(), cancelReq);
        assertTrue(response.getStatusCode() == HttpStatus.OK);
        solverResponse = toTest.getJobsForUser("bread");
        solvers = solverResponse.getBody();
        assertTrue(solvers.length == 0);
    }

    @Test
    public void simpleWiremockTest() throws Exception {
        // configureFor("localhost", 8082);
        stubFor(get("/test").willReturn(ok()));
        boolean result = toTest.simpleWiremockTest();
        assertTrue(result);
    }
}
