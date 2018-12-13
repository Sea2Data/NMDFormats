/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landings;

import LandingsTypes.v2.SeddellinjeType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class NoFilterTest {

    public NoFilterTest() {
    }

    /**
     * Test of findNext method, of class NoFilter.
     */
    @Test
    public void testNoFilter() throws Exception {
        System.out.println("NoFilter");
        LandingsHandler handler = new LandingsHandler();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ProductFilterTest.class.getClassLoader().getResourceAsStream("FDIR_HI_LSS_FANGST_2015_100_lines.psv")))) {
            NoFilter instance = new NoFilter();
            instance.setBaseIterator(handler.getPSViterator(reader));
            int n = 0;
            while (instance.hasNext()) {
                assertNotNull(instance.next().getProdukt().getKonserveringsmåteKode());
                n++;
            }
            assertEquals(99, n);
        }
    }

}
