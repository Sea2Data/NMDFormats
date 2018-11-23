/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class TemporalStratumTest {
    
    public TemporalStratumTest() {
    }

    /**
     * Test of addRange method, of class TemporalStratum.
     */
    @Test
    public void testAddRange() throws Exception {
        System.out.println("addRange");
        int startdate = 1;
        int enddate = 3;
        TemporalStratum instance = new TemporalStratum("s");
        instance.addRange(startdate, enddate);
        assertTrue(instance.inStratum(1));
        assertTrue(instance.inStratum(2));
        assertTrue(instance.inStratum(3));
        assertFalse(instance.inStratum(4));
        
    }

    /**
     * Test of inStratum method, of class TemporalStratum.
     */
    @Test
    public void testInStratum() throws StrataException {
        System.out.println("inStratum");
        int day = 0;
        TemporalStratum instance = new TemporalStratum("s");
        instance.addRange(4, 6);
        assertEquals(true, instance.inStratum(4));
        assertEquals(true, instance.inStratum(5));
        assertEquals(true, instance.inStratum(6));
        assertEquals(false, instance.inStratum(7));
        assertEquals(false, instance.inStratum(3));
    }

    /**
     * Test of getName method, of class TemporalStratum.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        TemporalStratum instance = new TemporalStratum("s");
        String expResult = "s";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
    
}
