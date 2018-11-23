/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class TemporalStrataTest {

    public TemporalStrataTest() {
    }

    private TemporalStrata mock() {
        List<TemporalStratum> s = new ArrayList<>();
        TemporalStrata ts = null;
        TemporalStratum q1 = new TemporalStratum("Q1");
        try {
            q1.addRange(1, 89);
            TemporalStratum q2 = new TemporalStratum("Q2");
            q2.addRange(90, 181);
            TemporalStratum q3 = new TemporalStratum("Q3");
            q3.addRange(182, 274);
            TemporalStratum q4 = new TemporalStratum("Q4");
            q4.addRange(275, 366);
            s.add(q1);
            s.add(q2);
            s.add(q3);
            s.add(q4);
            ts = new TemporalStrata(s);
        } catch (StrataException ex) {
            assert false;
        }

        return ts;

    }

    /**
     * Test of getStratum method, of class TemporalStrata.
     */
    @Test
    public void testGetStratum_int() throws StrataException {
        System.out.println("getStratum");
        int day = 0;
        TemporalStrata instance = this.mock();
        String expResult = "Q1";
        String result = instance.getStratum(2).getName();
        assertEquals(expResult, result);
        expResult = "Q4";
        result = instance.getStratum(300).getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStratum method, of class TemporalStrata.
     */
    @Test
    public void testGetStratum_XMLGregorianCalendar() throws Exception {
        System.out.println("getStratum");
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl(new GregorianCalendar());
        
        TemporalStrata instance = this.mock();
        date.setMonth(1);
        date.setDay(1);
        String expResult = "Q1";
        String result = instance.getStratum(date).getName();
        assertEquals(expResult, result);
        
        date.setMonth(4);
        expResult = "Q2";
        result = instance.getStratum(date).getName();
        assertEquals(expResult, result);
    }

}
