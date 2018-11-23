/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import Biotic.Biotic3.Biotic3Handler;
import BioticTypes.v3.CatchsampleType;
import BioticTypes.v3.FishstationType;
import BioticTypes.v3.MissionsType;
import java.io.File;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import partialRDBES.v1_16.BiologicalvariableType;
import partialRDBES.v1_16.DesignType;
import partialRDBES.v1_16.FishingtripType;
import partialRDBES.v1_16.LandingeventType;
import partialRDBES.v1_16.OnshoreeventType;
import partialRDBES.v1_16.SampleType;
import partialRDBES.v1_16.SamplingdetailsType;
import partialRDBES.v1_16.SpecieslistdetailsType;
import partialRDBES.v1_16.SpeciesselectionType;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESprovebatCompilerTest {
    
    public RDBESprovebatCompilerTest() {
    }

    private RDBESprovebatCompiler mock(){
        String pbpath = this.getClass().getClassLoader().getResource("pb_t2_test.xml").getFile();
        String resourcepath = RDBESprovebatCompilerTest.class.getClassLoader().getResource("rdbes_test_resources").getFile();
        
        MissionsType biotic = null;
        RDBESprovebatCompiler compiler = null;
        
        try{
        Biotic3Handler handler = new Biotic3Handler();
        biotic = handler.read(new File(pbpath));

        compiler = new RDBESprovebatCompiler(biotic, null, new DataConfigurations(new File(resourcepath)), 2016, false);
        }
        catch (Exception e){
            assert false;
        }
        return compiler;
    } 
    
        private RDBESprovebatCompiler mockWDummyConfig() throws Exception{
        String pbpath = this.getClass().getClassLoader().getResource("pb_t2_test.xml").getFile();
        String resourcepath = RDBESprovebatCompilerTest.class.getClassLoader().getResource("rdbes_test_resources").getFile();
        
        MissionsType biotic = null;
        RDBESprovebatCompiler compiler = null;
        
        //try{
        Biotic3Handler handler = new Biotic3Handler();
        biotic = handler.read(new File(pbpath));

        compiler = new RDBESprovebatCompiler(biotic, null, new DataConfigurationDummy(null), 2016, false);
        //}
        //catch (Exception e){
        //    assert false;
        //}
        return compiler;
    } 

    /**
     * Test of getSamplingDetails method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetSamplingDetails() throws Exception {
        System.out.println("getSamplingDetails");
        RDBESprovebatCompiler instance = null;
        SamplingdetailsType expResult = null;
        SamplingdetailsType result = instance.getSamplingDetails();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOnshoreEvent method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetOnshoreEvent() throws Exception {
        System.out.println("getOnshoreEvent");
        RDBESprovebatCompiler instance = null;
        OnshoreeventType expResult = null;
        OnshoreeventType result = instance.getOnshoreEvent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSpeciesSelectionDetails method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetSpeciesSelectionDetails() throws Exception {
        System.out.println("getSpeciesSelectionDetails");
        RDBESprovebatCompiler instance = null;
        SpecieslistdetailsType expResult = null;
        SpecieslistdetailsType result = instance.getSpeciesSelectionDetails();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLandingEvent method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetLandingEvent() throws Exception {
        System.out.println("getLandingEvent");
        RDBESprovebatCompiler instance = null;
        LandingeventType expResult = null;
        LandingeventType result = instance.getLandingEvent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSpeciesSelection method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetSpeciesSelection() {
        System.out.println("getSpeciesSelection");
        RDBESprovebatCompiler instance = null;
        SpeciesselectionType expResult = null;
        SpeciesselectionType result = instance.getSpeciesSelection();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFishingTrip method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetFishingTrip() {
        System.out.println("getFishingTrip");
        FishstationType fs = null;
        RDBESprovebatCompiler instance = null;
        FishingtripType expResult = null;
        FishingtripType result = instance.getFishingTrip();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSample method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetSample() {
        System.out.println("getSample");
        RDBESprovebatCompiler instance = null;
        SampleType expResult = null;
        SampleType result = instance.getSample();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBiologicalVariable method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetBiologicalVariable() throws Exception {
        System.out.println("getBiologicalVariable");
        RDBESprovebatCompiler instance = null;
        BiologicalvariableType expResult = null;
        BiologicalvariableType result = instance.getBiologicalVariable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addProveBatAsH5 method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testAddProveBatAsH5DummyConfigs() throws Exception {
        System.out.println("addProveBatAsH5");
        RDBESprovebatCompiler instance = mockWDummyConfig();
        instance.addProveBatAsH5();
        assertTrue(instance.rdbes.size()==1);
        DesignType d = (DesignType) instance.rdbes.get(0);
        assertTrue(d.getDEhierarchy()!=null);
        assertTrue(d.getSamplingdetails().getOnshoreevent().size()>5);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().size()>1);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().size()==1);
        assertTrue(d.getSamplingdetails().getOnshoreevent().get(0).getLandingevent().get(0).getSpeciesselection().get(0).getSample().size()>0);
    }

        /**
     * Test of addProveBatAsH5 method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testAddProveBatAsH5WConfigs() throws Exception {
        System.out.println("addProveBatAsH5");
        //RDBESprovebatCompiler instance = mock();
        //instance.addProveBatAsH5();
        //assertTrue(instance.rdbes.size()==1);
        fail("Test not implemented");
    }

    
    /**
     * Test of addProvebatOnshorevents method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testAddProvebatOnshorevents() throws Exception {
        System.out.println("addProvebatOnshorevents");
        SamplingdetailsType samplingdetails = null;
        RDBESprovebatCompiler instance = null;
        instance.addProvebatOnshorevents(samplingdetails);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addLeafSample method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testAddLeafSample() throws Exception {
        System.out.println("addLeafSample");
        SpeciesselectionType speciesSelection = null;
        CatchsampleType catchsample = null;
        RDBESprovebatCompiler instance = null;
        instance.addLeafSample(speciesSelection, catchsample);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProveBatStations method, of class RDBESprovebatCompiler.
     */
    @Test
    public void testGetProveBatStations() throws Exception {
        System.out.println("getProveBatStations");
        RDBESprovebatCompiler instance = null;
        List<FishstationType> expResult = null;
        List<FishstationType> result = instance.getProveBatStations();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
