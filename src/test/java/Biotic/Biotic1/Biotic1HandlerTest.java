/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Biotic.Biotic1;

import Biotic.Biotic1.Biotic1Handler;
import BioticTypes.v1_4.MissionsType;
import BioticTypes.v1_4.ObjectFactory;
import java.io.File;
import java.io.FileOutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class Biotic1HandlerTest {

    public Biotic1HandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRead() throws Exception {
        Biotic1Handler r = new Biotic1Handler();
        MissionsType m = r.read(Biotic1HandlerTest.class.getClassLoader().getResourceAsStream("test.xml"));
        assertTrue(m.getMission().get(0).getFishstation().size() > 0);
    }

    @Test
    public void testReadComp() throws Exception {
        Biotic1Handler r = new Biotic1Handler();
        MissionsType m = r.read(Biotic1HandlerTest.class.getClassLoader().getResourceAsStream("test_v1.xml"));
        assertTrue(m.getMission().get(0).getFishstation().size() > 0);
    }

    @Test
    public void testReadGarbage() {
        try {
            Biotic1Handler r = new Biotic1Handler();
            MissionsType m = r.read(Biotic1HandlerTest.class.getClassLoader().getResourceAsStream("biotic1_4.xsd.xml"));
            fail("Exceptione expected!");
        } catch (Exception e) {

        }
    }

    @Test
    public void testReadSave() throws Exception {
        ObjectFactory f = new ObjectFactory();
        Biotic1Handler r = new Biotic1Handler();

        File temp = File.createTempFile("biotic_example", ".tmp");
        temp.deleteOnExit();
        r.save(new FileOutputStream(temp), f.createMissionsType());

    }

}
