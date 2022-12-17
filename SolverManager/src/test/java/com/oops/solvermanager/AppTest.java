package com.oops.solvermanager;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.oops.solvermanager.Requests.ProblemRequest;

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
        stubFor(get("/tasks")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "[{\"username\": \"string\",\"solver\": 0,\"taskId\": \"string\",\"solverTimestamp\": \"2022-12-15T12:25:37.706Z\",\"maxMemory\": 0,\"mzn\": \"string\",\"dzn\": \"string\",\"vcpu\": 0}]")));
        assertTrue(true);
    }

    @Test
    public void simpleWiremockTest() throws Exception {
        // configureFor("localhost", 8082);
        stubFor(get("/test").willReturn(ok()));
        boolean result = toTest.simpleWiremockTest();
        assertTrue(result);
    }
}
