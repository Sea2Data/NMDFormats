/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landings;

import LandingsTypes.v2.LandingsdataType;
import LandingsTypes.v2.SeddellinjeType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class LandingsHandlerTest {
    
    public LandingsHandlerTest() {
    }

    @Test
    public void testRead() throws Exception{
        System.out.println("readLanding");
        LandingsHandler h = new LandingsHandler();
        InputStream xml = LandingsHandlerTest.class.getClassLoader().getResourceAsStream("landinger_100_lines.xml");
        LandingsdataType landings = h.read(xml);
        assertNotNull(landings.getSeddellinje().get(0).getArtKode());
        assertEquals(landings.getSeddellinje().get(0).getSisteFangstdato().getYear(), 2015);
        assertEquals(landings.getSeddellinje().get(0).getProduksjon().getLandingsdato().getYear(), 2015);
    }
    
    @Test
    public void testSaveLanding() throws Exception {
        System.out.println("saveLanding");
        InputStream xml = LandingsHandlerTest.class.getClassLoader().getResourceAsStream("landinger_100_lines.xml");
    
        LandingsHandler instance = new LandingsHandler();
        LandingsdataType result = instance.read(xml);
        assertTrue(result.getSeddellinje().size() > 0);
        xml.close();

        File temp = File.createTempFile("landing_example", ".tmp");
        temp.deleteOnExit();
        instance.save(new FileOutputStream(temp), result);

        InputStream re = new FileInputStream(temp);
        LandingsdataType result_re = instance.read(re);

        assertEquals(result.getSeddellinje().size(), result_re.getSeddellinje().size());
    }
    
    @Test
    public void testPSBIterator() throws Exception{
        LandingsHandler instance = new LandingsHandler();
        InputStream psv = LandingsHandlerTest.class.getClassLoader().getResourceAsStream("FDIR_HI_LSS_FANGST_2015_100_lines.psv");
        BufferedReader r = new BufferedReader(new InputStreamReader(psv));
        Iterator<SeddellinjeType> it = instance.getPSViterator(r);
        int count =0;
        while(it.hasNext()){
            SeddellinjeType s = it.next();
            assertNotNull(s.getArt());
            count++;
        }
        assertEquals(count, 99);
        r.close();
    }
}
