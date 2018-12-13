/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landings;

import LandingsTypes.v2.SeddellinjeType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class ProductFilterTest {
    
    public ProductFilterTest() {
    }

    /**
     * Test of findNext method, of class ProductFilter.
     */
    @Test
    public void ProductFilter() throws Exception{
        LandingsHandler handler = new LandingsHandler();
        System.out.println("productfilter");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ProductFilterTest.class.getClassLoader().getResourceAsStream("FDIR_HI_LSS_FANGST_2015_100_lines.psv")))) {
            Set<String> s = new HashSet<>();
            s.add("02");
            ProductFilter instance = new ProductFilter(s);
            instance.setBaseIterator(handler.getPSViterator(reader));
            int n=0;
            while (instance.hasNext()){
                assertEquals("02", instance.next().getProdukt().getKonserveringsmåteKode());
                n++;
            }
            assertTrue(n<98);
            assertTrue(n>50);
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ProductFilterTest.class.getClassLoader().getResourceAsStream("FDIR_HI_LSS_FANGST_2015_100_lines.psv")))) {
            Set<String> s = new HashSet<>();
            s.add("03");
            ProductFilter instance = new ProductFilter(s);
            instance.setBaseIterator(handler.getPSViterator(reader));
            int n=0;
            while (instance.hasNext()){
                assertEquals("03", instance.next().getProdukt().getKonserveringsmåteKode());
                n++;
            }
            assertTrue(n<20);
        }
    }
    
}
