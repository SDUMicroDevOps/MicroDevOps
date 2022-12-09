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
        //Get all privilages
        Controller controller = new Controller();
        Controller.initSQL();
        List<Privilage> dbPrivilages = controller.allPrivilages();
        assertTrue(dbPrivilages.size() == 3);

        //Get one privilage
        Privilage privilage1 = dbPrivilages.get(0);
        Privilage privilage2 = controller.getPrivilageById(privilage1.getId()).getBody();
        assertTrue(privilage1.equals(privilage2));
    }
}
