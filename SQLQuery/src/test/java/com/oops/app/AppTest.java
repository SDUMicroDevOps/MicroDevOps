package com.oops.app;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.oops.app.responseType.Privilage;

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
        Controller controller = new Controller();
        Controller.initSQL();
        List<Privilage> dbPribilages = controller.allPrivilages();
        assertTrue(dbPribilages.size() == 4);
    }
}
